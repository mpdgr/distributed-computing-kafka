package com.mpdgr.jobcontroller.domain;

import lombok.Getter;

public class JobEvent {
    @Getter
    protected String jobId;

    protected JobEvent(String jobId) {
        this.jobId = jobId;
    }
}
