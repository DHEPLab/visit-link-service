package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.config.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProjectDTO {

    @NotNull
    private String name;

    private Integer status;
}
