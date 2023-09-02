package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ComputationSystemException;
import com.mpdgr.jobcontroller.domain.JobCompleteEvent;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

//@Component
//@Scope("prototype")
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class JobCompleteEventListener implements ApplicationListener<JobCompleteEvent> {
    private final ComputationJob job;
    private final CompletableFuture<JobCompleteSummary> futureSummary;
    private String jobId;


    public void logJob(){
        //for debugging only
        log.debug("Listening for job id: {}", jobId);
        log.debug("Listening for job: {}, listener hash: {}", this.job, this.hashCode());
    }


    @Override
    public void onApplicationEvent(JobCompleteEvent event) {
        log.info("Application event noticed");
        log.debug("Processing listener event: {}", event);
        log.debug("Processing listener job: {}", job);
        logJob();
        if (event.getJobId().equals(job.getJobId())){
            try {
                log.debug("Future complete attempt for job: {}", job.getJobId());
                JobCompleteEventHandler jobCompleteEventHandler = new JobCompleteEventHandler();
                futureSummary.complete(jobCompleteEventHandler.handleJobCompleted(job, event));
                log.debug("Future completed for job: {}", job.getJobId());
            } catch (ComputationSystemException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
