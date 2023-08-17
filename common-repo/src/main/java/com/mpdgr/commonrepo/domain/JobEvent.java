package com.mpdgr.commonrepo.domain;

import lombok.Getter;

public class JobEvent {
    @Getter
    protected String jobId;

    protected JobEvent(String jobId) {
        this.jobId = jobId;
    }
}
