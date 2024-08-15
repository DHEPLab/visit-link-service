package edu.stanford.fsi.reap.web.rest.admin;

import static com.aliyun.oss.internal.OSSConstants.DEFAULT_OBJECT_CONTENT_TYPE;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/oss")
public class OSSResource {

  private final OSS ossClient;

  @Value("${application.oss.bucket-name}")
  private String bucketName;

  public OSSResource(OSS ossClient) {
    this.ossClient = ossClient;
  }

  @GetMapping("/pre-signed-url")
  public PreSignedURLWrapper sign(@RequestParam String format) {
    GeneratePresignedUrlRequest request =
        new GeneratePresignedUrlRequest(bucketName, generateFilename(format), HttpMethod.PUT);
    // URL expire date is 5 minutes
    Date expiration = new Date(new Date().getTime() + 3600 * 5);
    request.setExpiration(expiration);
    request.setContentType(DEFAULT_OBJECT_CONTENT_TYPE);
    URL url = ossClient.generatePresignedUrl(request);
    return new PreSignedURLWrapper(url.toString());
  }

  private String generateFilename(String format) {
    return UUID.randomUUID().toString().replace("-", "") + "." + format;
  }

  @AllArgsConstructor
  @Getter
  static class PreSignedURLWrapper {
    private final String url;
  }
}
