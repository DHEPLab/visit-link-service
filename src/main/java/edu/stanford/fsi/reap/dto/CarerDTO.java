package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.entity.Baby;
import edu.stanford.fsi.reap.entity.enumerations.FamilyTies;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/** @author hookszhang */
@Data
public class CarerDTO {

  @NotNull
  @Size(min = 2, max = 10)
  private String name;

  @NotNull
  @Pattern(regexp = "^1\\d{10}", message = "请输入11位手机号")
  private String phone;

  @Size(max = 20)
  private String wechat;

  @NotNull private FamilyTies familyTies;

  private boolean master = false;

  @NotNull private Baby baby;
}
