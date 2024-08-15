package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.pojo.Question;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
public class QuestionnaireRequestDTO {

  private Long id;

  @NotNull
  @Size(max = 20)
  private String name;

  @Valid
  @NotEmpty
  @Size(max = 20) // 问题限制20个，超出20前端提示
  private List<Question> questions;

}
