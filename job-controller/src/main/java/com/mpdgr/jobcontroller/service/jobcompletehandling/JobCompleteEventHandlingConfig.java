package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;

import java.util.concurrent.CompletableFuture;

@Configuration
@Slf4j
public class JobCompleteEventHandlingConfig {
    @Autowired
    private ApplicationEventMulticaster applicationEventMulticaster;

    public void registerJobCompleteEventListener(ComputationJob job,
                                                 CompletableFuture<JobCompleteSummary> futureSummary) {
        JobCompleteEventListener listener = new JobCompleteEventListener(job, futureSummary);
        applicationEventMulticaster.addApplicationListener(listener);
        log.debug("Event listener registered, job id {}", job.getJobId());
    }
    //todo: unregister
}
