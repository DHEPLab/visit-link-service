package edu.stanford.fsi.reap.service;

import com.google.common.collect.Lists;
import edu.stanford.fsi.reap.converter.StringListConverter;
import edu.stanford.fsi.reap.dto.ErrDTO;
import edu.stanford.fsi.reap.entity.*;
import edu.stanford.fsi.reap.entity.Module;
import edu.stanford.fsi.reap.entity.enumerations.*;
import edu.stanford.fsi.reap.pojo.Domain;
import edu.stanford.fsi.reap.pojo.VisitReportObjData;
import edu.stanford.fsi.reap.repository.*;
import edu.stanford.fsi.reap.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class ExcelService {

    private final UserRepository userRepository;
    private final CarerRepository carerRepository;
    private final ModuleRepository moduleRepository;
    private final BabyRepository babyRepository;
    private final CommunityHouseWorkerRepository communityHouseWorkerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagService tagService;
    private final CommunityHouseWorkerRepository chwRepository;

    public ExcelService(UserRepository userRepository, CarerRepository carerRepository, ModuleRepository moduleRepository, BabyRepository babyRepository, CommunityHouseWorkerRepository communityHouseWorkerRepository, PasswordEncoder passwordEncoder, TagService tagService, CommunityHouseWorkerRepository chwRepository) {
        this.userRepository = userRepository;
        this.carerRepository = carerRepository;
        this.moduleRepository = moduleRepository;
        this.babyRepository = babyRepository;
        this.communityHouseWorkerRepository = communityHouseWorkerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tagService = tagService;
        this.chwRepository = chwRepository;
    }

    /**
     * 家访记录
     */
    public byte[] writeExcel(List<VisitReportObjData> visitReportObjData) {
        return getDataReport(visitReportObjData, this::mapByteArray);
    }

    private byte[] getDataReport(List<VisitReportObjData> visitReportObjData, Function<Workbook, byte[]> saveFileOrOther) {
        try (
                InputStream inputStream = getTemplateResourceIO("static/excel/Healthy-Future-Report.xlsx");
                Workbook workBook = new XSSFWorkbook(inputStream)
        ) {

            addContentRow(workBook, visitReportObjData);

            return saveFileOrOther.apply(workBook);
        } catch (Exception e) {
            log.error("addContentRow ", e);
            throw new RuntimeException("导出失败，服务器异常，数据可能不全");
        }
    }

    private void addContentRow(Workbook workbook, List<VisitReportObjData> allData) {
        Sheet sheet = workbook.getSheetAt(0);
        int index = 1;
        for (VisitReportObjData itemRow : allData) {
            Row row = sheet.createRow(index);
            int cellInt = 0;

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getId());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getVisitTime().toString());

            if (itemRow.getVisit().getStartTime() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getStartTime().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getCompleteTime() == null ? "" : itemRow.getVisit().getCompleteTime().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getStatus() == VisitStatus.UNDONE ? "Unfinished" : itemRow.getVisit().getStatus().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getDeleted() ? "是" : "否");
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getDistance() == null ? 0 : itemRow.getVisit().getDistance());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getId());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getRemark());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getName());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getNumber());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getDescription());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getStage() == BabyStage.BIRTH ? "Born" : "Pregnant");
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getEndOfApplicableDays());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLesson().getStartOfApplicableDays());

            Curriculum curriculum = itemRow.getVisit().getBaby().getCurriculum();
            if (curriculum != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getCurriculum().getName());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            Module module;
            for (int i = 0; i < 7; i++) {
                if (itemRow.getModules() != null) {
                    try {
                        module = itemRow.getModules().get(i);
                        if (module != null) {
                            row.createCell(cellInt++).setCellValue(module.getNumber() + "/" + getTopicCN(module.getTopic()));
                        } else {
                            row.createCell(cellInt++).setCellValue("");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        row.createCell(cellInt++).setCellValue("");
                    }
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }
            }

            QuestionnaireRecord questionnaireRecord;
            for (int i = 0; i < 20; i++) {
                if (itemRow.getQuestionnaireRecords() != null) {
                    try {
                        questionnaireRecord = itemRow.getQuestionnaireRecords().get(i);
                        if (questionnaireRecord != null) {
                            row.createCell(cellInt++).setCellValue(questionnaireRecord.getTitleNo() + "." + questionnaireRecord.getName());
                            row.createCell(cellInt++).setCellValue(questionnaireRecord.getAnswer());
                        } else {
                            row.createCell(cellInt++).setCellValue("");
                            row.createCell(cellInt++).setCellValue("");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        row.createCell(cellInt++).setCellValue("");
                        row.createCell(cellInt++).setCellValue("");
                    }
                } else {
                    row.createCell(cellInt++).setCellValue("");
                    row.createCell(cellInt++).setCellValue("");
                }
            }

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getId());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getChw().getIdentity());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getCreatedAt().toString());
            row.createCell(cellInt++).setCellValue(userRepository.findOneByUsername(itemRow.getVisit().getCreatedBy()).orElse(User.builder().build()).getRealName());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLastModifiedAt().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getLastModifiedBy());

            CommunityHouseWorker chw = itemRow.getVisit().getChw().getChw();
            if (chw != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getChw().getIdentity());

                User supervisor = chw.getSupervisor();
                if (supervisor != null) {
                    row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getChw().getSupervisor().getRealName());
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }

                if (itemRow.getVisit().getChw().getChw().getTags() != null) {
                    row.createCell(cellInt++).setCellValue(new StringListConverter().convertToDatabaseColumn(itemRow.getVisit().getChw().getChw().getTags()));
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }

                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getChw().getCreatedAt().toString());
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getChw().getChw().getCreatedBy());
            } else {
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getName());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getIdentity());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getGender().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getLocation());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getRemark());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getArea());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getStage() == BabyStage.BIRTH ? "Born" : "Pregnant");

            if (itemRow.getVisit().getBaby().getEdc() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getEdc().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            if (itemRow.getVisit().getBaby().getBirthday() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getBirthday().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getAssistedFood() ? "YES" : "NO");

            if (itemRow.getVisit().getBaby().getFeedingPattern() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getFeedingPattern().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            if (itemRow.getVisit().getBaby().getActionFromApp() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getActionFromApp().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getApproved());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getCloseAccountReason());
            row.createCell(cellInt++).setCellValue(itemRow.getVisit().getBaby().getCreatedAt().toString());
            row.createCell(cellInt++).setCellValue(
                    userRepository.findOneByUsername(itemRow.getVisit().getBaby().getCreatedBy()).orElse(User.builder().build()).getRealName()
            );

            Carer carer;
            boolean isNextToCell = false;
            if (itemRow.getCarers() != null) {
                carer = itemRow.getCarers().stream().filter(Carer::isMaster).findFirst().orElse(null);
                if (carer != null) {
                    row.createCell(cellInt++).setCellValue(carer.getName() + "/" + carer.getPhone() + "/" + carer.getFamilyTies().toString() + "/主看护人/" + carer.getWechat());
                    itemRow.getCarers().remove(carer);
                } else {
                    setCarers(itemRow, row, cellInt, 4);
                    isNextToCell = true;
                }
                if (!isNextToCell) {
                    setCarers(itemRow, row, cellInt, 3);
                }
            } else {
                for (int i = 0; i < 4; i++)
                    row.createCell(cellInt++).setCellValue("");
            }

            index++;
        }
    }

    /**
     * Baby Roster
     */
    public byte[] writeBabyRoster(List<Baby> babies) {
        return getDataReportBaby(babies, this::mapByteArray);
    }

    private byte[] getDataReportBaby(List<Baby> babies, Function<Workbook, byte[]> saveFileOrOther) {
        try (
                InputStream inputStream = getTemplateResourceIO("static/excel/Healthy-Future-Report-Baby.xlsx");
                Workbook workBook = new XSSFWorkbook(inputStream)
        ) {

            addContentRowByBaby(workBook, babies);

            return saveFileOrOther.apply(workBook);
        } catch (Exception e) {
            log.error("addContentRow ", e);
            throw new RuntimeException("导出失败，服务器异常，数据可能不全");
        }
    }

    private void addContentRowByBaby(Workbook workbook, List<Baby> allData) {
        Sheet sheet = workbook.getSheetAt(0);
        int index = 1;
        for (Baby itemRow : allData) {
            Row row = sheet.createRow(index);
            int cellInt = 0;

            row.createCell(cellInt++).setCellValue(itemRow.getId());
            row.createCell(cellInt++).setCellValue(itemRow.getIdentity());

            if (itemRow.isDeleted()) {
                row.createCell(cellInt++).setCellValue("Archived");
            } else {
                row.createCell(cellInt++).setCellValue("Enrolled");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getCloseAccountReason());

            BabyStage babyStage = itemRow.getStage();
            if (babyStage.equals(BabyStage.EDC))
                row.createCell(cellInt++).setCellValue("Unborn");
            else
                row.createCell(cellInt++).setCellValue("Born");

            row.createCell(cellInt++).setCellValue(itemRow.getName());
            row.createCell(cellInt++).setCellValue(itemRow.getGender().toString());

            LocalDate localDate = itemRow.getBirthday();
            if (localDate != null) {
                row.createCell(cellInt++).setCellValue(localDate.toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getArea());
            row.createCell(cellInt++).setCellValue(itemRow.getLocation());

            Chw chw = itemRow.getChw();
            if (chw != null) {
                row.createCell(cellInt++).setCellValue(chw.getId());
                row.createCell(cellInt++).setCellValue(chw.getChw().getIdentity());
                row.createCell(cellInt++).setCellValue(chw.getRealName());
                row.createCell(cellInt++).setCellValue(chw.getPhone());
            } else {
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
            }

            List<Carer> carers = carerRepository.findByBabyIdOrderByMasterDesc(itemRow.getId());
            if (carers != null && carers.size() > 0) {
                for (int i = 0; i < 5; i++) {
                    if (i >= carers.size()) {
                        // 没有看护人了 填空
                        for (int j = 0; j < 5; j++)
                            row.createCell(cellInt++).setCellValue("");
                    } else {
                        // 5个一组填
                        Carer carer = carers.get(i);
                        row.createCell(cellInt++).setCellValue(carer.isMaster());
                        row.createCell(cellInt++).setCellValue(carer.getName());
                        FamilyTies familyTies = carer.getFamilyTies();
                        if (familyTies != null) {
                            row.createCell(cellInt++).setCellValue(familyTies.toString());
                        } else {
                            row.createCell(cellInt++).setCellValue("");
                        }
                        row.createCell(cellInt++).setCellValue(carer.getPhone());
                        row.createCell(cellInt++).setCellValue(carer.getWechat());
                    }
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++)
                        row.createCell(cellInt++).setCellValue("");
                }
            }

            row.createCell(cellInt++).setCellValue(itemRow.getRemark());
            row.createCell(cellInt++).setCellValue(itemRow.getEdc() == null ? "" : itemRow.getEdc().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getCreatedAt().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getCreatedBy());
            row.createCell(cellInt++).setCellValue(itemRow.getStage().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getAssistedFood());

            FeedingPattern feedingPattern = itemRow.getFeedingPattern();
            if (feedingPattern != null) {
                row.createCell(cellInt++).setCellValue(feedingPattern.toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            ActionFromApp actionFromApp = itemRow.getActionFromApp();
            if (actionFromApp != null)
                row.createCell(cellInt++).setCellValue(actionFromApp.toString());
            else
                row.createCell(cellInt++).setCellValue("");

            row.createCell(cellInt++).setCellValue(itemRow.getApproved());

            index++;
        }
    }

    /**
     * CHW
     *
     * @param chwList
     */
    public byte[] writeChwExcel(List<UnfilteredUser> chwList) {
        return getDataReportChw(chwList, this::mapByteArray);
    }

    private byte[] getDataReportChw(List<UnfilteredUser> chwList, Function<Workbook, byte[]> saveFileOrOther) {
        try (
                InputStream inputStream = getTemplateResourceIO("static/excel/Healthy-Future-Report-CHW.xlsx");
                Workbook workBook = new XSSFWorkbook(inputStream)
        ) {

            addContentRowByCommunityHouseWorkers(workBook, chwList);

            return saveFileOrOther.apply(workBook);
        } catch (Exception e) {
            log.error("addContentRow ", e);
            throw new RuntimeException("导出失败，服务器异常，数据可能不全");
        }
    }

    private void addContentRowByCommunityHouseWorkers(Workbook workbook, List<UnfilteredUser> chwList) {
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < chwList.size(); i++) {
            UnfilteredUser user = chwList.get(i);
            CommunityHouseWorker chw = user.getChw();
            User supervisor = chw.getSupervisor();
            List<String> tags = user.getChw().getTags();

            Row row = sheet.createRow(i + 1);
            int column = 0;

            // CHW ID
            row.createCell(column++).setCellValue(chw.getIdentity());
            // CHW NAME
            row.createCell(column++).setCellValue(user.getRealName());
            // CHW Phone number
            row.createCell(column++).setCellValue(user.getPhone());
            // CHW Status
            row.createCell(column++).setCellValue(user.isDeletedString());
            // CHW System ID
            row.createCell(column++).setCellValue(user.getId());
            // Supervisor ID of this CHW
            Cell supervisorIdCell = row.createCell(column++);
            if (supervisor != null) {
                supervisorIdCell.setCellValue(supervisor.getId());
            }

            // Tag of this CHW's working area
            Cell tagCell = row.createCell(column++);
            if (tags != null) {
                tagCell.setCellValue(String.valueOf(tags));
            }

            // CHW created date
            row.createCell(column++).setCellValue(user.getCreatedAt().toString());
            // CHW created by who
            row.createCell(column).setCellValue(user.getCreatedBy());
        }
    }

    private void setCarers(VisitReportObjData itemRow, Row row, int cellInt, int num) {
        for (int i = 0; i < num; i++) {
            try {
                Carer carer = itemRow.getCarers().get(i);
                if (carer != null && !carer.isMaster()) {
                    row.createCell(cellInt++).setCellValue(carer.getName() + "/" + carer.getPhone() + "/" + carer.getFamilyTies().toString() + "/非主看护人/" + carer.getWechat());
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }
            } catch (IndexOutOfBoundsException e) {
                row.createCell(cellInt++).setCellValue("");
            }
        }
    }

    private byte[] mapByteArray(Workbook workBook) {
        try (
                FileOutputStream out = new FileOutputStream("/home/jbf/Downloads/" + UUID.randomUUID().toString() + ".xlsx")
        ) {
            workBook.write(out);
            return null;
        } catch (IOException e) {
            log.error("work book to byte array ", e);
            throw new RuntimeException("work book to byte array " + e.getMessage());
        }
    }

    private InputStream getTemplateResourceIO(String url) {
        ClassPathResource classPathResource = new ClassPathResource(url);
        try {
            return classPathResource.getInputStream();
        } catch (Exception e) {
            log.error("excel模板读取失败", e);
            throw new RuntimeException("excel模板读取失败");
        }
    }

    public byte[] writeNotStartExcel(List<ExportVisit> visit) {
        return getNotStartDataReport(visit, this::mapByteArray);
    }

    private byte[] getNotStartDataReport(List<ExportVisit> visitReportObjData, Function<Workbook, byte[]> saveFileOrOther) {
        try (
                InputStream inputStream = getTemplateResourceIO("static/excel/Healthy-Future-Report-Not-Finished-Visit.xlsx");
                Workbook workBook = new XSSFWorkbook(inputStream)
        ) {

            addNotStartContentRow(workBook, visitReportObjData);

            return saveFileOrOther.apply(workBook);
        } catch (Exception e) {
            log.error("addContentRow ", e);
            throw new RuntimeException("导出失败，服务器异常，数据可能不全");
        }
    }

    private void addNotStartContentRow(Workbook workbook, List<ExportVisit> allData) {
        Sheet sheet = workbook.getSheetAt(0);
        int index = 1;
        for (ExportVisit itemRow : allData) {
            Row row = sheet.createRow(index);
            int cellInt = 0;

            row.createCell(cellInt++).setCellValue(itemRow.getId());
            row.createCell(cellInt++).setCellValue(itemRow.getVisitTime().toString());

            if (itemRow.getStartTime() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getStartTime().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getCompleteTime() == null ? "" : itemRow.getCompleteTime().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getStatus() == VisitStatus.UNDONE ? "Unfinished" : itemRow.getStatus().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getDeleted() ? "是" : "否");
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getId());
            row.createCell(cellInt++).setCellValue(itemRow.getRemark());
            row.createCell(cellInt++).setCellValue(itemRow.getDeleteReason());
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getName());
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getNumber());
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getDescription());
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getStage() == BabyStage.BIRTH ? "Born" : "Pregnant");
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getEndOfApplicableDays());
            row.createCell(cellInt++).setCellValue(itemRow.getLesson().getStartOfApplicableDays());

            Curriculum curriculum = itemRow.getBaby().getCurriculum();
            if (curriculum != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getBaby().getCurriculum().getName());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(Integer.toString(itemRow.getNextModuleIndex() + 1));
            Domain module;
            for (int i = 0; i < 7; i++) {
                if (itemRow.getLesson().getModules() != null) {
                    try {
                        module = itemRow.getLesson().getModules().get(i);
                        if (module != null) {
                            Module module1 = moduleRepository.findById(Long.parseLong(module.getValue())).get();
                            row.createCell(cellInt++).setCellValue(module1.getNumber().toString() + "/" + getTopicCN(module1.getTopic()));
                        } else {
                            row.createCell(cellInt++).setCellValue("");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        row.createCell(cellInt++).setCellValue("");
                    }
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }
            }

            for (int i = 0; i < 20; i++) {
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
            }

            if (itemRow.getChw() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getChw().getId());
                row.createCell(cellInt++).setCellValue(itemRow.getChw().getChw().getIdentity());
            } else {
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
            }
            row.createCell(cellInt++).setCellValue(itemRow.getCreatedAt().toString());
            row.createCell(cellInt++).setCellValue(userRepository.findOneByUsername(itemRow.getCreatedBy()).orElse(User.builder().build()).getRealName());
            row.createCell(cellInt++).setCellValue(itemRow.getLastModifiedAt().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getLastModifiedBy());

            if (itemRow.getChw() != null) {
                CommunityHouseWorker chw = itemRow.getChw().getChw();
                row.createCell(cellInt++).setCellValue(itemRow.getChw().getChw().getIdentity());

                User supervisor = chw.getSupervisor();
                if (supervisor != null) {
                    row.createCell(cellInt++).setCellValue(itemRow.getChw().getChw().getSupervisor().getRealName());
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }

                if (itemRow.getChw().getChw().getTags() != null) {
                    row.createCell(cellInt++).setCellValue(new StringListConverter().convertToDatabaseColumn(itemRow.getChw().getChw().getTags()));
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }

                row.createCell(cellInt++).setCellValue(itemRow.getChw().getChw().getCreatedAt().toString());
                row.createCell(cellInt++).setCellValue(itemRow.getChw().getChw().getCreatedBy());
            } else {
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getName());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getIdentity());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getGender().toString());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getLocation());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getRemark());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getArea());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getStage() == BabyStage.BIRTH ? "Born" : "Pregnant");

            if (itemRow.getBaby().getEdc() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getBaby().getEdc().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            if (itemRow.getBaby().getBirthday() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getBaby().getBirthday().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getAssistedFood() ? "YES" : "NO");

            if (itemRow.getBaby().getFeedingPattern() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getBaby().getFeedingPattern().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            if (itemRow.getBaby().getActionFromApp() != null) {
                row.createCell(cellInt++).setCellValue(itemRow.getBaby().getActionFromApp().toString());
            } else {
                row.createCell(cellInt++).setCellValue("");
            }

            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getApproved());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getCloseAccountReason());
            row.createCell(cellInt++).setCellValue(itemRow.getBaby().getCreatedAt().toString());
            row.createCell(cellInt++).setCellValue(
                    userRepository.findOneByUsername(itemRow.getBaby().getCreatedBy()).orElse(User.builder().build()).getRealName()
            );

            Carer carer;
            boolean isNextToCell = false;
            List<Carer> carerList = carerRepository.findAllByBabyId(itemRow.getBaby().getId());
            if (carerList.size() != 0) {
                carer = carerList.stream().filter(Carer::isMaster).findFirst().orElse(null);
                if (carer != null) {
                    row.createCell(cellInt++).setCellValue(carer.getName() + "/" + carer.getPhone() + "/" + carer.getFamilyTies().toString() + "/主看护人/" + carer.getWechat());
                    carerList.remove(carer);
                } else {
                    setNotStartCarers(carerList, row, cellInt, 4);
                    isNextToCell = true;
                }
                if (!isNextToCell) {
                    setNotStartCarers(carerList, row, cellInt, 3);
                }
            } else {
                for (int i = 0; i < 4; i++)
                    row.createCell(cellInt++).setCellValue("");
            }
            index++;
        }
    }

    private void setNotStartCarers(List<Carer> carerList, Row row, int cellInt, int num) {
        for (int i = 0; i < num; i++) {
            try {
                Carer carer = carerList.get(i);
                if (carer != null && !carer.isMaster()) {
                    row.createCell(cellInt++).setCellValue(carer.getName() + "/" + carer.getPhone() + "/" + carer.getFamilyTies().toString() + "/非主看护人/" + carer.getWechat());
                } else {
                    row.createCell(cellInt++).setCellValue("");
                }
            } catch (IndexOutOfBoundsException e) {
                row.createCell(cellInt++).setCellValue("");
            }
        }
    }

    public String getTopicCN(ModuleTopic topic) {
        switch (topic) {
            case MOTHER_NUTRITION:
                return "母亲营养";
            case BREASTFEEDING:
                return "母乳喂养";
            case BABY_FOOD:
                return "婴儿辅食";
            case INFANT_INJURY_AND_PREVENTION:
                return "婴儿伤病和预防";
            case CAREGIVER_MENTAL_HEALTH:
                return "照料人心理健康";
            case GOVERNMENT_SERVICES:
                return "政府服务";
            case KNOWLEDGE_ATTITUDE_TEST:
                return "知识态度检测";
            default:
                return "其他";
        }
    }

    public void importBabyLocations(MultipartFile records) {
        try (Workbook workBook = new XSSFWorkbook(records.getInputStream())) {
            handleBabyRecordRow(workBook.getSheetAt(0));
        } catch (Exception e) {
            log.error("importContentRow ", e);
            throw new RuntimeException("导入失败，服务器异常，数据可能有问题！");
        }
    }

    @Transactional
    public void importChws(MultipartFile records) {
        try (Workbook workBook = new XSSFWorkbook(records.getInputStream())) {
            handleChwRecordRow(workBook.getSheetAt(0));
        } catch (Exception e) {
            log.error("importContentRow ", e);
        }
    }

    private void handleBabyRecordRow(Sheet sheet) {
        int endRow = sheet.getLastRowNum() + 1;
        if (endRow <= 1) {
            return;
        }
        Set<Long> babyIds = new HashSet<>();
        Map<Long, List<Double>> babyLocationMap = new HashMap<>();
        for (int i = 1; i < endRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                if (null != row.getCell(0) && null != row.getCell(1) && null != row.getCell(2)) {
                    Long babyId = Long.valueOf(row.getCell(0).getStringCellValue());
                    double longitude = row.getCell(1).getNumericCellValue();
                    double latitude = row.getCell(2).getNumericCellValue();
                    babyLocationMap.put(2L, Lists.newArrayList(longitude, latitude));
                    babyIds.add(babyId);
                }
            }
        }
        List<Baby> selectBabies = babyRepository.findAllById(babyIds);
        if (!CollectionUtils.isEmpty(selectBabies)) {
            for (Baby baby : selectBabies) {
                if (babyLocationMap.containsKey(baby.getId())) {
                    List<Double> locations = babyLocationMap.get(baby.getId());
                    if (locations.size() == 2) {
                        baby.setLongitude(locations.get(0));
                        baby.setLatitude(locations.get(1));
                        baby.setShowLocation(true);
                        babyRepository.save(baby);
                    }
                }
            }
        }
    }


    private ErrDTO getDTO(String name, Integer number, String matters) {
        ErrDTO errDTO = new ErrDTO();
        errDTO.setMatters(matters);
        errDTO.setName(name);
        errDTO.setNumber(number);
        return errDTO;
    }

    private void handleChwRecordRow(Sheet sheet) {
        int endRow = sheet.getLastRowNum() + 1;
        for (int i = 2; i < endRow; i++) {
            if (StringUtils.isEmpty(sheet.getRow(i).getCell(0).getStringCellValue())) {
                break;
            }
            Row row = sheet.getRow(i);
            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
            row.getCell(3).setCellType(CellType.STRING);
            row.getCell(4).setCellType(CellType.STRING);
            row.getCell(5).setCellType(CellType.STRING);
            String realName = row.getCell(0).getStringCellValue();
            String identity = row.getCell(1).getStringCellValue();
            String tag = row.getCell(2).getStringCellValue();
            String phone = row.getCell(3).getStringCellValue();
            String username = row.getCell(4).getStringCellValue();
            String password = row.getCell(5).getStringCellValue();
            String[] split = tag.split(",");
            String encryptedPassword = passwordEncoder.encode(password);
            List<String> tags = Arrays.asList(split);

            if (!StringUtils.isEmpty(realName) &&
                !StringUtils.isEmpty(identity) &&
                !communityHouseWorkerRepository.findFirstByIdentity(identity).isPresent() &&
                !StringUtils.isEmpty(tag) &&
                !StringUtils.isEmpty(phone) &&
                phone.matches("\\d{11}") &&
                !StringUtils.isEmpty(username) &&
                !userRepository.findOneByUsername(username).isPresent() &&
                !StringUtils.isEmpty(password)) {
                CommunityHouseWorker communityHouseWorker = new CommunityHouseWorker(null, identity, tags, null);
                communityHouseWorker.setProjectId(SecurityUtils.getProjectId());
                tagService.saveAll(tags);
                chwRepository.save(communityHouseWorker);
                User chw = new User(null, username, encryptedPassword, realName, phone,
                        "ROLE_CHW",
                        LocalDateTime.now(),
                        communityHouseWorker
                );
                chw.setProjectId(SecurityUtils.getProjectId());
                userRepository.save(chw);
            }
        }
    }


    public Map<String, Object> checkChws(MultipartFile records) {
        try (Workbook workBook = new XSSFWorkbook(records.getInputStream())) {
            return checkChwRecordRow(workBook.getSheetAt(0));
        } catch (Exception e) {
            log.error("importContentRow ", e);
            throw new RuntimeException("导入失败，服务器异常，数据可能有问题！");
        }
    }

    private Map<String, Object> checkChwRecordRow(Sheet sheet) {
        Map<String, Object> map = new HashMap<>();

        List<ErrDTO> errDTOS = new ArrayList<>();
        int endRow = sheet.getPhysicalNumberOfRows() + 1;
        if (endRow <= 1) {
            return map;
        }

        int a = 0;
        for (int i = 2; i < endRow; i++) {
            if (StringUtils.isEmpty(sheet.getRow(i).getCell(0).getStringCellValue())) {
                break;
            }
            Row row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellType(CellType.STRING);
                row.getCell(1).setCellType(CellType.STRING);
                row.getCell(2).setCellType(CellType.STRING);
                row.getCell(3).setCellType(CellType.STRING);
                row.getCell(4).setCellType(CellType.STRING);
                row.getCell(5).setCellType(CellType.STRING);
                String realName = row.getCell(0).getStringCellValue();
                String identity = row.getCell(1).getStringCellValue();
                String tag = row.getCell(2).getStringCellValue();
                String phone = row.getCell(3).getStringCellValue();
                String username = row.getCell(4).getStringCellValue();
                String password = row.getCell(5).getStringCellValue();

                if (StringUtils.isEmpty(realName) &&
                        StringUtils.isEmpty(identity) &&
                        StringUtils.isEmpty(tag) &&
                        StringUtils.isEmpty(phone) &&
                        StringUtils.isEmpty(username) &&
                        StringUtils.isEmpty(password)
                )
                    break;

                a++;
                if (StringUtils.isEmpty(realName)) {
                    errDTOS.add(getDTO(realName, (i - 1), "真实姓名必填"));
                    continue;
                }
                if (StringUtils.isEmpty(identity)) {
                    errDTOS.add(getDTO(realName, (i - 1), "社区工作者ID必填"));
                    continue;
                }
                if (communityHouseWorkerRepository.findFirstByIdentity(identity).isPresent()) {
                    errDTOS.add(getDTO(realName, (i - 1), "ID: " + identity + " 已经存在"));
                    continue;
                }
                if (StringUtils.isEmpty(tag)) {
                    errDTOS.add(getDTO(realName, (i - 1), "所在地区必填"));
                    continue;
                }
                String[] split = tag.split(",");
                if (split.length > 3) {
                    errDTOS.add(getDTO(realName, (i - 1), "街道或乡镇最多只能添加3个"));
                    continue;
                }
                if (StringUtils.isEmpty(phone)) {
                    errDTOS.add(getDTO(realName, (i - 1), "联系电话必填"));
                    continue;
                }
                if (!phone.matches("\\d{11}")) {
                    errDTOS.add(getDTO(realName, (i - 1), "社区工作者电话:11位手机号码"));
                    continue;
                }
                if (StringUtils.isEmpty(username)) {
                    errDTOS.add(getDTO(realName, (i - 1), "账户名称必填"));
                    continue;
                }
                if (userRepository.findOneByUsername(username).isPresent()) {
                    errDTOS.add(getDTO(realName, (i - 1), "username: " + username + " 已经存在"));
                    continue;
                }

                if (StringUtils.isEmpty(password)) {
                    errDTOS.add(getDTO(realName, (i - 1), "账户密码必填"));
                }
            }
        }
        map.put("errData", errDTOS);
        map.put("total", a);
        return map;
    }
}