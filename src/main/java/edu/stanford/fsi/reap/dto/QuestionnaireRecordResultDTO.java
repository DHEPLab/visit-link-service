package edu.stanford.fsi.reap.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class QuestionnaireRecordResultDTO implements Serializable {

  private List<Long> questionnaireRecordIds;

}
