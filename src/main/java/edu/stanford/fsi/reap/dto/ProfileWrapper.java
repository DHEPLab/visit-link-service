package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ProfileWrapper {
  @NotNull
  @Size(min = 2, max = 10)
  private String realName;

  @NotNull
  @Pattern(regexp = "^1\\d{10}", message = "请输入11位手机号")
  private String phone;

}