package com.mpdgr.taskscheduler.service;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This class keeps track of tasks passed to workers and returned results.
 * It's goal is to monitor progress of computations within each job
 */
@Component
public class JobsRegistry {

    /**
     * Monitoring map holds the information about how many
     * tasks were passed to given workers and how many were returned.
     * It is used to determine which worker should be supported by
     * supercomputer (app which can do all types of computations and is
     * assigned to support least effective worker)
     * map key - job ID
     * map value - current state of tasks included in a job
     */
    private final HashMap<String, ProgressReport> monitoringMap = new HashMap<>();

    boolean jobRegistered(ComputationEvent event){
        return monitoringMap.containsKey(event.getJobId());
    }

    String registerJob(ComputationEvent event){
        String id = event.getJobId();
        monitoringMap.put(id, new ProgressReport(id));
        return id;
    }

    ProgressReport getProgressReport(String jobId){
        return monitoringMap.get(jobId);
    }
}
