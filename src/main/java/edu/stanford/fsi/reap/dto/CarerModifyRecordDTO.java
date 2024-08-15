package edu.stanford.fsi.reap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarerModifyRecordDTO {

    private String userName;

    private String roleName;

    private List<String> columnName;

    private List<String> oldValue;

    private List<String> newValue;

    private LocalDateTime lastModifiedAt;
}
