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
    private final SuperWorkerMonitor superWorkerMonitor;
    private final TopicRouter topicRouter;

    /* minimum nr of unprocessed tasks at which the worker qualifies for superworker assistance */
    @Value("${computing.properties.assist:2}")
    private Integer minLag;


    public ComputationEvent scheduleTask(ComputationEvent event)
            throws ProgressReportMissingException, JsonProcessingException {

        /* check whether incoming task belongs to any of the jobs registered by scheduler */
        boolean isRegistered = registry.jobRegistered(event);
        if (!isRegistered) {
            registry.registerJob(event);
        }

        /* assign task to one of standard workers or to the superworker */
        return qualifiesForSuperworker(event) ?
                assignToSuperworker(event) : assignToStandardWorker(event);
    }


    private boolean qualifiesForSuperworker(ComputationEvent event)
            throws ProgressReportMissingException {

        /* check whether current workers' lag/delay at job execution
        exceeds min for superworker assistance */

        log.debug("Checking progress report, job id {}", event.getJobId());
        ProgressReport report = registry.getProgressReport(event.getJobId());

        if (report == null) {
            throw new ProgressReportMissingException("Progress report is null");
        }

        boolean assistanceRequired = report.getMaxLagValue() >= minLag;
        if (!assistanceRequired){
            return false;
        }

        /* check whether this task type is the one with the highest lag/delay */
        boolean assistanceRequiredAtThisTask = (report.getMaxLaggingOperation() == event.getTask().getType());
        log.debug("Checked if assistance required, required: {}", assistanceRequiredAtThisTask);
        if (!assistanceRequiredAtThisTask){
            return false;
        }

        /* check whether superworker is idle
        (new task can only be assigned if all current tasks of superworker are completed) */
        boolean superworkerAvailable = superWorkerMonitor.isIdle();
        log.debug("Checked if superworker is idle, idle: {}", superworkerAvailable);

        if (!superworkerAvailable){
            log.info("Assistance required, but superworker not available");
            return false;
        }

        return true;
    }

    private ComputationEvent assignToStandardWorker(ComputationEvent event) throws JsonProcessingException {
        log.info("Assigning event to standard worker; event: {}", event);
        topicRouter.sendToStandardWorker(event);
        return event;
    }

    private ComputationEvent assignToSuperworker(ComputationEvent event) throws JsonProcessingException {
        log.info("Assigning event to superworker; event: {}", event);
        topicRouter.sendToSuperworker(event);
        return event;
    }
}
