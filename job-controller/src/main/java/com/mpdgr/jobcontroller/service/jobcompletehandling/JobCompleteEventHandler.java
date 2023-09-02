package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ComputationSystemException;
import com.mpdgr.jobcontroller.domain.JobCompleteEvent;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class JobCompleteEventHandler {
    public JobCompleteSummary handleJobCompleted(ComputationJob computationJob, JobCompleteEvent completeEvent)
            throws ComputationSystemException {

        log.debug("Handling job completed, job: {}, event {}", computationJob, completeEvent);
        log.info("Handling job completed, id {}", completeEvent.getJobId());
        JobCompleteSummary jobSummary = new JobCompleteSummary(completeEvent.getJobId());
        jobSummary.processJobCompleteEvent(completeEvent, computationJob.getStartTime());

        if (computationJob.getJobSize() != jobSummary.totalTasksCompleted()) {
            throw new ComputationSystemException(String.format(
                    "Nr of completed tasks (%s) does not match number of assigned tasks (%s)",
                    jobSummary.totalTasksCompleted(), computationJob.getJobSize()));
        }

        return jobSummary;
    }
}
