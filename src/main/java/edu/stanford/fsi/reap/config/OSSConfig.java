package edu.stanford.fsi.reap.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

  @Value("${application.oss.end-point}")
  private String endpoint;

  @Value("${application.oss.access-key-id}")
  private String accessKeyId;

  @Value("${application.oss.access-key-secret}")
  private String accessKeySecret;

  @Bean
  public OSS getOss() {
    return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
  }
}
