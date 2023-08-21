package com.mpdgr.jobcontroller.domain;

import org.springframework.context.ApplicationEvent;

public class JobCompleteEvent extends ApplicationEvent {

    public JobCompleteEvent(Object source) {
        super(source);
    }
}
