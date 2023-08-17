package com.mpdgr.taskscheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.ProgressReportMissingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskSchedulerService {
    private final JobsRegistry registry;
    private final SuperComputerMonitor superComputerMonitor;
    private final TopicRouter topicRouter;

    //minimum nr of unprocessed tasks at which
    // the worker qualifies for supercomputer assistance
    @Value("${computing.properties.minlagassist}:2")
    private Integer minLag;


    public ComputationEvent scheduleTask(ComputationEvent event)
            throws ProgressReportMissingException, JsonProcessingException {
        //check whether incoming task belongs to any of the registered jobs, if not add a new job
        boolean isRegistered = registry.jobRegistered(event);
        if (!isRegistered) {
            registry.registerJob(event);
        }

        //assign task to one of standard workers or to the supercomputer
        return qualifiesForSupercomputer(event) ? assignToSuperComputer(event) : assignToStandardWorker(event);
    }


    private boolean qualifiesForSupercomputer(ComputationEvent event)
            throws ProgressReportMissingException {

        //check whether current workers' lag/delay at job execution exceeds min for supercomputer assistance

        ProgressReport report = registry.getProgressReport(event.getJobId());
        if (report == null) {
            throw new ProgressReportMissingException("Progress report is null");
        }

        boolean assistanceRequired = report.getMaxLagValue() >= minLag;
        if (!assistanceRequired){
            return false;
        }

        //check whether this task type is the one with highest lag/delay

        boolean assistanceRequiredAtThisTask = (report.getMaxLaggingOperation() == event.getTask().getType());
        if (!assistanceRequiredAtThisTask){
            return false;
        }

        //check whether supercomputer is idle (new task can only be assigned if all current tasks are completed)

        boolean supercomputerAvailable = superComputerMonitor.isIdle();
        if (!supercomputerAvailable){
            log.info("Assistance required, but supercomputer not available");
            return false;
        }

        return true;
    }

    private ComputationEvent assignToStandardWorker(ComputationEvent event) throws JsonProcessingException {
        log.info("Assigning event to standard worker; event: {}", event);
        topicRouter.sendToStandardWorker(event);
        return event;
    }

    private ComputationEvent assignToSuperComputer(ComputationEvent event) throws JsonProcessingException {
        log.info("Assigning event to supercomputer; event: {}", event);
        topicRouter.sendToSupercomputer(event);
        return event;
    }
}
