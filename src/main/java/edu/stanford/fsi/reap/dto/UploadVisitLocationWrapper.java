package edu.stanford.fsi.reap.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UploadVisitLocationWrapper {

    private Long visitId;

    private Long babyId;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

}
