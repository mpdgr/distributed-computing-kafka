package com.mpdgr.jobcontroller.domain;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@Setter
public class JobCompleteEvent extends ApplicationEvent {
    private final String jobId;
    private final List<ComputationEvent> events;

    public JobCompleteEvent(Object source, String jobId, List<ComputationEvent> events) {
        super(source);
        this.jobId = jobId;
        this.events = events;
    }
}
