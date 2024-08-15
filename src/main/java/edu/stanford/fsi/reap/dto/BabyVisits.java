package edu.stanford.fsi.reap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BabyVisits {
  private List<VisitResultDTO> started;
  private List<VisitResultDTO> notStarted;
  private int numberOfNoRemark;

}