package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class RemarkWrapper {
  @NotNull
  @Size(max = 500)
  private String remark;

}