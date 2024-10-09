package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.entity.enumerations.FamilyTies;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @author huey
 */
@Data
public class AppCarerDTO {

  @NotNull
  @Size(min = 2, max = 10)
  private String name;

  @NotNull
  private String phone;

  @Size(max = 20)
  private String wechat;

  @NotNull
  private FamilyTies familyTies;

  private boolean master = false;
}
