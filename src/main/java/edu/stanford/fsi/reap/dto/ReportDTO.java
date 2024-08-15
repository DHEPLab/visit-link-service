package edu.stanford.fsi.reap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ReportDTO implements Serializable {

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDay;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDay;

  public boolean validDay(){
    return startDay.isBefore(endDay.plusDays(1));
  }

  public LocalDateTime getStartTime() {
    return LocalDateTime.of(startDay, LocalTime.MIN);
  }

  public LocalDateTime getEndTime() {
    return LocalDateTime.of(endDay, LocalTime.MAX);
  }

}
