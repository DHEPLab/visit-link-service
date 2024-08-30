package edu.stanford.fsi.reap.web.rest.admin;

import edu.stanford.fsi.reap.entity.PreSignedURLWrapper;
import edu.stanford.fsi.reap.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/oss")
public class OSSResource {
    private final FileService fileService;

    public OSSResource(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload-pre-signed-url")
    public PreSignedURLWrapper generatePreSignedUrlForUpload(@RequestParam String format) {
        return new PreSignedURLWrapper(fileService.generatePresignedUrlForUpload(format).toString());
    }
}
