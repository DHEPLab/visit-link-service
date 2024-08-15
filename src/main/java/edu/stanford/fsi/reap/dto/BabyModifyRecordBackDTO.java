package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.entity.enumerations.BabyStage;
import edu.stanford.fsi.reap.entity.enumerations.FeedingPattern;
import edu.stanford.fsi.reap.entity.enumerations.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BabyModifyRecordBackDTO {

    private LocalDateTime lastModifiedAt;

    private String realName;

    private Gender gender;

    private BabyStage stage;

    private LocalDate birthday;

    private Boolean assistedFood;

    private FeedingPattern feedingPattern;

    private String area;

    private String location;

    private Double longitude;

    private Double latitude;

    private String remark;
}
