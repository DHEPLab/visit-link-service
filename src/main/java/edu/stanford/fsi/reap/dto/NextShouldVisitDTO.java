package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.entity.Lesson;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NextShouldVisitDTO {

  private List<LocalDate> visitDateRange;
  private Lesson lesson;
  

}
