package edu.stanford.fsi.reap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
  @Bean
  public S3Client s3Client() {
    return S3Client.builder().credentialsProvider(DefaultCredentialsProvider.create()).build();
  }
}
