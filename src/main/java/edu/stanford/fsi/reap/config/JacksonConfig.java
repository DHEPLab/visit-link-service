package edu.stanford.fsi.reap.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.stanford.fsi.reap.converter.LocalDateTimeDeserializer;
import edu.stanford.fsi.reap.converter.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer() {
    return builder -> {
      SimpleModule module = new SimpleModule();

      module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
      module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
      module.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
      module.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);

      builder.modules(module);
    };
  }
}
