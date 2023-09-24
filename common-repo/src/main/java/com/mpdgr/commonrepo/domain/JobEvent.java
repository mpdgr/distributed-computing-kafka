package com.mpdgr.commonrepo.domain;

import lombok.Getter;
import lombok.Setter;

public class JobEvent {
    @Getter
    @Setter
    protected String jobId;

    protected JobEvent(String jobId) {
        this.jobId = jobId;
    }
}
