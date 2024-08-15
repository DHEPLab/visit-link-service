package edu.stanford.fsi.reap.web.rest.admin;

import edu.stanford.fsi.reap.dto.ModuleDTO;
import edu.stanford.fsi.reap.dto.ModuleRequestDTO;
import edu.stanford.fsi.reap.entity.Module;
import edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch;
import edu.stanford.fsi.reap.repository.LessonRepository;
import edu.stanford.fsi.reap.repository.ModuleRepository;
import edu.stanford.fsi.reap.security.SecurityUtils;
import edu.stanford.fsi.reap.service.ModuleService;
import edu.stanford.fsi.reap.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch.MASTER;

/**
 * @author hookszhang
 */
@RestController
@RequestMapping("/admin/modules")
@Slf4j
public class ModuleResource {

    private final LessonRepository lessonRepository;
    private final ModuleRepository repository;
    private final ModuleService service;
    private final ModelMapper modelMapper;

    public ModuleResource(
            LessonRepository lessonRepository, ModuleRepository repository, ModuleService service,
            ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.repository = repository;
        this.service = service;
        this.modelMapper = modelMapper;
    }

    /**
     * Create/Update a module, module status is PUBLISHED
     *
     * @param dto dto
     */
    @PostMapping
    public void publishModule(@Valid @RequestBody ModuleRequestDTO dto) {

        Module curModule = repository.findByName(dto.getName());
        if (curModule != null) {
            if (dto.getId() != null) {
                if (!curModule.getId().equals(dto.getId())) {
                    throw new BadRequestAlertException("名称" + dto.getName() + "已存在");
                }
            } else {
                throw new BadRequestAlertException("名称" + dto.getName() + "已存在");
            }
        }

        Module module = modelMapper.map(dto, Module.class);
        repository
                .findByNumberAndBranch(module.getNumber(), MASTER)
                .ifPresent(
                        master -> {
                            if (module.getId() != null) {
                                Optional<Module> optional = repository.findById(module.getId());
                                if (optional.isPresent() && optional.get().version(master)) {
                                    return;
                                }
                            }
                            throwNumberAlreadyExistException(module);
                        });

        module.setComponents(service.cleanUpTheExtraPageFooter(module.getComponents()));
        service.publish(module);
    }

    /**
     * Create/Update a module, module status is DRAFT
     *
     * @param dto dto
     */
    @PostMapping("/draft")
    public Module saveModuleDraft(@Valid @RequestBody ModuleRequestDTO dto) {
        Module curModule = repository.findByName(dto.getName());
        if (curModule != null) {
            if (dto.getId() != null) {
                if (!curModule.getId().equals(dto.getId())) {
                    throw new BadRequestAlertException("名称" + dto.getName() + "已存在");
                }
            } else {
                throw new BadRequestAlertException("名称" + dto.getName() + "已存在");
            }
        }

        Module module = modelMapper.map(dto, Module.class);
        if (module.getId() == null
                && repository.findByNumberAndBranch(module.getNumber(), MASTER).isPresent()) {
            return throwNumberAlreadyExistException(module);
        }

        module.setComponents(service.cleanUpTheExtraPageFooter(module.getComponents()));
        return service.draft(module);
    }

    private Module throwNumberAlreadyExistException(Module module) {
        throw new BadRequestAlertException("编号" + module.getNumber() + "已经存在");
    }

    @GetMapping
    public Page<ModuleDTO> getModules(String search, Boolean published, Pageable pageable) {
        return repository.findBySearch(search, published, SecurityUtils.getProjectId(), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModule(@PathVariable Long id) {
        return repository
                .findById(id)
                .map(
                        module -> {
                            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
                            if (MASTER.equals(module.getBranch())) {
                                repository
                                        .findOneByVersionKeyAndBranch(module.getVersionKey(), CurriculumBranch.DRAFT)
                                        .ifPresent(
                                                draft -> {
                                                    builder.header("x-draft-id", String.valueOf(draft.getId()));
                                                    builder.header("x-draft-date", draft.getLastModifiedAt().toString());
                                                });
                            }
                            return builder.body(module);
                        })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable Long id) {
        repository
                .findById(id)
                .ifPresent(
                        module -> {
                            if (MASTER.equals(module.getBranch()) && module.isPublished()) {
                                Long count = lessonRepository.countByModuleId(id);
                                log.info("Delete master branch module, id {} , Number of USES {}", id, count);
                                if (count > 0) {
                                    throw new BadRequestAlertException("不能删除已在课堂中使用的模块");
                                }
                            }
                            repository.deleteById(id);
                        });
    }
}
