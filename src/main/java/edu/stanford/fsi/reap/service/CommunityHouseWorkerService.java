package edu.stanford.fsi.reap.service;

import edu.stanford.fsi.reap.entity.UnfilteredUser;
import edu.stanford.fsi.reap.repository.UnfilteredUserRepository;
import edu.stanford.fsi.reap.repository.UserRepository;
import edu.stanford.fsi.reap.security.AuthoritiesConstants;
import edu.stanford.fsi.reap.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommunityHouseWorkerService {

    private final UnfilteredUserRepository unfilteredUserRepository;
    private final ExcelService excelService;

    public CommunityHouseWorkerService(
            UnfilteredUserRepository unfilteredUserRepository,
            UserRepository userRepository, ExcelService excelService) {
        this.unfilteredUserRepository = unfilteredUserRepository;
        this.excelService = excelService;
    }

    /**
     * 导出报表 CHW
     */
    public byte[] chwReport(String lang) {
        Long projectId = SecurityUtils.getProjectId();
        List<UnfilteredUser> chwList = unfilteredUserRepository.findByRoleOrderByCreatedAtDesc(AuthoritiesConstants.CHW)
                .stream()
                .filter(target -> target.getProjectId().equals(projectId))
                .collect(Collectors.toList());
        return excelService.writeChwExcel(chwList, lang);
    }
}
