package com.mpdgr.jobcontroller.domain;

import org.springframework.context.ApplicationEvent;

public class JobCompletedEvent extends ApplicationEvent {


    public JobCompletedEvent(Object source) {
        super(source);
    }
}
