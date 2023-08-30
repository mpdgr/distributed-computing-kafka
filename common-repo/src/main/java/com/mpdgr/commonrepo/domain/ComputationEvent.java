package com.mpdgr.commonrepo.domain;

import com.mpdgr.commonrepo.enumeration.WorkerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ComputationEvent extends JobEvent{
    private String workerId;
    private WorkerType workerType; //todo: parse type in worker
    private int taskNr;
    private ComputationTask task;

    public ComputationEvent(String jobId) {
        super(jobId);
    }

    public ComputationEvent(String jobId, String workerId,
                            WorkerType workerType, int taskNr,
                            ComputationTask task) {
        super(jobId);
        this.workerId = workerId;
        this.workerType = workerType;
        this.taskNr = taskNr;
        this.task = task;
    }
}
