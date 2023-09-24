package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
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

    @Override
    public void onApplicationEvent(JobCompleteEvent event) {
        log.info("Application event noticed, job id: {}", event.getJobId());
        if (event.getJobId().equals(job.getJobId())) {
            JobCompleteEventHandler jobCompleteEventHandler = new JobCompleteEventHandler();
            futureSummary.complete(jobCompleteEventHandler.handleJobCompleted(job, event));
            log.debug("Future completed for job: {}", job.getJobId());
            config.removeListener(this);
        }
    }

    /* listeners compared on jobId only */
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
