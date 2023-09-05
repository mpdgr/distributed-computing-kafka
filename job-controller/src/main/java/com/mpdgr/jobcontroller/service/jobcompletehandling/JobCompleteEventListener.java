package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ComputationSystemException;
import com.mpdgr.jobcontroller.domain.JobCompleteEvent;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class JobCompleteEventListener implements ApplicationListener<JobCompleteEvent> {
    private final String jobId;
    private final ComputationJob job;
    private final CompletableFuture<JobCompleteSummary> futureSummary;
    private final JobCompleteEventHandlingConfig config;

//    public void logJob(){
//        //for debugging only
//        log.debug("Listening for job id: {}", jobId);
//        log.debug("Listening for job: {}, listener hash: {}", this.job, this.hashCode());
//    }

    @Override
    public void onApplicationEvent(JobCompleteEvent event) {
        log.info("Application event noticed, job id: {}", event.getJobId());
        log.debug("Processing listener job: {}", job);
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
            config.removeListener(this);
        }
    }

    //listeners compared on jobId only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobCompleteEventListener listener = (JobCompleteEventListener) o;

        return jobId.equals(listener.jobId);
    }

    @Override
    public int hashCode() {
        return jobId.hashCode();
    }
}
