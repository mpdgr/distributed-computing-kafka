package com.mpdgr.taskscheduler.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SuperWorkerMonitor {
    //indicates whether superworker is idle and new tasks can be assigned
    private volatile boolean isIdle = false;
}
