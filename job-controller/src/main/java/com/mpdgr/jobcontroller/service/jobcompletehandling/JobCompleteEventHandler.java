package com.mpdgr.jobcontroller.service.jobcompletehandling;

import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.SystemException;
import com.mpdgr.jobcontroller.domain.JobCompleteEvent;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobCompleteEventHandler {
    public JobCompleteSummary handleJobCompleted(ComputationJob computationJob,
                                                 JobCompleteEvent completeEvent) {

        log.info("HANDLING JOB COMPLETED!, id {}", completeEvent.getJobId());
        JobCompleteSummary jobSummary = new JobCompleteSummary(completeEvent.getJobId());
        jobSummary.processJobCompleteEvent(completeEvent, computationJob.getStartTime());

        if (computationJob.getJobSize() != jobSummary.totalTasksCompleted()) {
            throw new SystemException(String.format(
                    "Nr of completed tasks (%s) does not match number of assigned tasks (%s)",
                    jobSummary.totalTasksCompleted(), computationJob.getJobSize()));
        }

        return jobSummary;
    }
}
