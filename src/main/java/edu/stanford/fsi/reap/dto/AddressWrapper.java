package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class AddressWrapper {
  @NotNull
  @Size(max = 100)
  private String area;

  @NotNull
  @Size(max = 200)
  private String location;

}