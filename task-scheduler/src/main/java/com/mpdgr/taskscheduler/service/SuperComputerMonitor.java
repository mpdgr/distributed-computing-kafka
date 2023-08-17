package com.mpdgr.taskscheduler.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SuperComputerMonitor {
    //indicates whether supercomputer is idle and new tasks can be assigned
    private boolean isIdle = false;
}
