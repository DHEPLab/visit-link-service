package edu.stanford.fsi.reap.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileResource {

  @Value("${application.oss.host}")
  private String ossHost;

  @GetMapping("/{filename}")
  public ResponseEntity<?> redirectToOss(@PathVariable String filename) {
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .header("Location", ossHost + "/" + filename)
        .build();
  }
}
