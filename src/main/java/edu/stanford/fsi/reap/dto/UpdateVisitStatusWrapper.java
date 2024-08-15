package edu.stanford.fsi.reap.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.stanford.fsi.reap.entity.QuestionnaireRecord;
import edu.stanford.fsi.reap.entity.enumerations.VisitStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateVisitStatusWrapper {

  private Integer nextModuleIndex;

  @NotNull
  private VisitStatus visitStatus;

  private LocalDateTime startTime;

  @Valid
  private List<QuestionnaireRecord> questionnaireRecords;

}