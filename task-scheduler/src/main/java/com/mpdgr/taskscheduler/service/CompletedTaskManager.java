package com.mpdgr.taskscheduler.service;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.ProgressReportMissingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompletedTaskManager {
    private final JobsRegistry registry;

    public ComputationEvent registerCompletedTask(ComputationEvent event)
            throws ProgressReportMissingException {

        ProgressReport progressReport = registry.getProgressReport(event.getJobId());
        if (progressReport == null) {
            log.error("No progress report for job id: {}", event.getJobId());
            throw new ProgressReportMissingException(
                    String.format("No progress report for job id: %s", event.getJobId()));
        }

        ComputationType completedTaskType = event.getTask().getType();
        switch (completedTaskType) {
            case ADDITION -> progressReport.registerCompletedAddition();
            case MULTIPLICATION -> progressReport.registerCompletedMultiplication();
            case DIVISION -> progressReport.registerCompletedDivision();
            case EXPONENT -> progressReport.registerCompletedExponent();
        }

        log.debug("Updated progress report for job id: {}, operation type: {}",
                event.getJobId(), event.getTask().getType().name());

        return event;
    }
}
