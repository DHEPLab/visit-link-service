package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitTimeWrapper {

  @NotNull
  private LocalDateTime visitTime;

}