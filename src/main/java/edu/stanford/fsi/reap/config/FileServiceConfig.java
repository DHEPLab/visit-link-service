package edu.stanford.fsi.reap.config;

import com.aliyun.oss.OSS;
import edu.stanford.fsi.reap.service.FileService;
import edu.stanford.fsi.reap.service.AliyunFileService;
import edu.stanford.fsi.reap.service.AWSFileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class FileServiceConfig {
    @Bean
    @ConditionalOnProperty(name = "application.file.storage-type", havingValue = "oss")
    public FileService aliyunFileService(OSS ossClient) {
        return new AliyunFileService(ossClient);
    }

    @Bean
    @ConditionalOnProperty(name = "application.file.storage-type", havingValue = "s3")
    public FileService awsFileService(S3Client s3Client) {
        return new AWSFileService(s3Client);
    }
}
