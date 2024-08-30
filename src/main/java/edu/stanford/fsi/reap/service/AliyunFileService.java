package edu.stanford.fsi.reap.service;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Date;

import static com.aliyun.oss.internal.OSSConstants.DEFAULT_OBJECT_CONTENT_TYPE;

@Slf4j
public class AliyunFileService extends FileService {
    private final OSS ossClient;

    @Value("${application.oss.bucket-name}")
    private String bucketName;

    @Value("${application.file.expiration}")
    private int expiration;

    @Value("${application.oss.host}")
    private String ossHost;

    public AliyunFileService(OSS ossClient) {
        this.ossClient = ossClient;
    }

    @Override
    public URL generatePresignedUrlForUpload(String format) {
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, generateFilename(format), HttpMethod.PUT);
        Date expirationDate = new Date(new Date().getTime() + 3600L * expiration);
        request.setExpiration(expirationDate);
        request.setContentType(DEFAULT_OBJECT_CONTENT_TYPE);
        return ossClient.generatePresignedUrl(request);
    }

    @Override
    public String generatePresignedUrlForDownload(String key) {
        return this.ossHost + "/" + key;
    }
}
