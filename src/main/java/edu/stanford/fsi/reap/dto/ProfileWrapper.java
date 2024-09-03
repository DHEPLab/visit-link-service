package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileWrapper {
  @NotNull @Size(min = 2, max = 10)
  private String realName;

  @NotNull private String phone;
}
