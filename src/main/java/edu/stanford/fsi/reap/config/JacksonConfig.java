package edu.stanford.fsi.reap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.fsi.reap.converter.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());

    mapper.registerModule(module);
    return mapper;
  }
}
