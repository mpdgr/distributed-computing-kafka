package com.mpdgr.taskscheduler.domain;

import lombok.Data;

@Data
public class ComputationTask {
    ComputationType type;
    double value1;
    double value2;
    double result;

    public ComputationTask(ComputationType type, double value1, double value2) {
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
    }
}
