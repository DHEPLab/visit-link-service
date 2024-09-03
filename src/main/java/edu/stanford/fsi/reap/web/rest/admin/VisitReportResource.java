package edu.stanford.fsi.reap.web.rest.admin;

import edu.stanford.fsi.reap.dto.ReportDTO;
import edu.stanford.fsi.reap.service.BabyService;
import edu.stanford.fsi.reap.service.CommunityHouseWorkerService;
import edu.stanford.fsi.reap.service.VisitReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/admin/report")
public class VisitReportResource {

    private final VisitReportService visitReportService;
    private final BabyService babyService;
    private final CommunityHouseWorkerService communityHouseWorkerService;

    public VisitReportResource(VisitReportService visitReportService, BabyService babyRosterReport, CommunityHouseWorkerService communityHouseWorkerService) {
        this.visitReportService = visitReportService;
        this.babyService = babyRosterReport;
        this.communityHouseWorkerService = communityHouseWorkerService;
    }

    @GetMapping
    public ResponseEntity<byte[]> reportExcel(@Valid ReportDTO reportDTO) {
        byte[] ret = visitReportService.report(reportDTO);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID().toString() + ".xlsx")
                .body(ret);
    }

    @GetMapping("/babyRosterReport")
    public ResponseEntity<byte[]> babyRosterReport() {
        byte[] ret = babyService.babyRosterReport();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID().toString() + ".xlsx")
                .body(ret);
    }

    @GetMapping("/chwReport")
    public ResponseEntity<byte[]> chwReport(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "en") String lang
    ) {
        byte[] ret = communityHouseWorkerService.chwReport(lang);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID().toString() + ".xlsx")
                .body(ret);

    }
}
