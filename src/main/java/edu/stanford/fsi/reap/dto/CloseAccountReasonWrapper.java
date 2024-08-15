package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CloseAccountReasonWrapper {
  @NotNull
  @Size(max = 100)
  private String reason;

}