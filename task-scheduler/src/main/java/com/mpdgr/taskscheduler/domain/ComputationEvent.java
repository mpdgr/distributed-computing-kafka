package com.mpdgr.taskscheduler.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ComputationEvent extends JobEvent {
    private String workerId;
    private int taskNr;
    private ComputationTask task;

    public ComputationEvent(String jobId) {
        super(jobId);
    }
}
