package edu.stanford.fsi.reap.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BabyModifyRecordDTO {

    private String userName;

    private String roleName;

    private List<String> columnName;

    private List<Object> oldValue;

    private List<Object> newValue;

    private LocalDateTime lastModifiedAt;
}
