package com.mpdgr.jobcontroller.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class ComputationJob {
    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private final long MAX_REQUEST_NR = 10_000L;

    private final String jobId = UUID.randomUUID().toString();

    @NotNull
    @Max(MAX_REQUEST_NR)
    private Integer addition;

    @NotNull
    @Max(MAX_REQUEST_NR)
    private Integer multiplication;

    @NotNull
    @Max(MAX_REQUEST_NR)
    private Integer division;

    @NotNull
    @Max(MAX_REQUEST_NR)
    private Integer exponent;

    @JsonIgnore
    @Setter
    private Long startTime;

    @JsonIgnore
    @Setter
    private Long endTime;

    @JsonIgnore
    public long getJobSize(){
        return addition + multiplication + division + exponent;
    }
}
