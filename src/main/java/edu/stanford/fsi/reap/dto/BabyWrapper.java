package edu.stanford.fsi.reap.dto;

import javax.validation.constraints.NotNull;
import edu.stanford.fsi.reap.entity.enumerations.BabyStage;
import edu.stanford.fsi.reap.entity.enumerations.FeedingPattern;
import edu.stanford.fsi.reap.entity.enumerations.Gender;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class BabyWrapper {
  @NotNull
  @Size(min = 2, max = 10)
  private String name;

  @NotNull private Gender gender;

  @NotNull private BabyStage stage;

  private LocalDate edc;

  private LocalDate birthday;

  private FeedingPattern feedingPattern;

  private Boolean assistedFood = false;

}