package com.mpdgr.jobcontroller.service;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.jobcontroller.service.jobcompletehandling.JobCompleteEvent;
import com.mpdgr.jobcontroller.service.jobcompletehandling.JobCompleteEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * All incoming computation events with completed tasks are stored in a completedEventsMap
 * map key - jobId
 * map value - list of completed events for respective job
 * <p>
 * jobSizes map holds information about how many tasks were assigned to each job
 * New job is saved in both maps once the controller receives computation request.
 * <p>
 * Each time new completed ComputationEvent is saved in the completedEventsMap,
 * respective list size is checked. Once the size matches expected nr of tasks for job completion,
 * event is initiated and results are passed to the controller.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class ResultsRegistry {
    private final JobCompleteEventPublisher eventsPublisher;

    private final Map<String, List<ComputationEvent>> completedEventsMap = new ConcurrentHashMap<>();
    private final Map<String, Long> jobSizes = new ConcurrentHashMap<>();

    boolean addCompletedEvent(ComputationEvent event) throws ResultsRegistryException {
        String jobId = event.getJobId();
        if (!completedEventsMap.containsKey(jobId)){
            throw new ResultsRegistryException(String
                    .format("Completed events map doesn't contain job key: %s", jobId));
        }
        List<ComputationEvent> completedForJob = completedEventsMap.get(event.getJobId());
        completedForJob.add(event);
        completedEventsMap.put(event.getJobId(), completedForJob);
        log.debug("Completed task recorded for job id: {}", jobId);
        if(jobComplete(jobId, (long) completedForJob.size())){
            //produce completed event
            produceJobCompleteEvent(event.getJobId());
        }
        return true;
    }

    private boolean jobComplete(String jobId, Long completed) throws ResultsRegistryException {
        if (!jobSizes.containsKey(jobId)){
            throw new ResultsRegistryException(String
                    .format("Job sizes map doesn't contain job key: %s", jobId));
        }
        return jobSizes.get(jobId).equals(completed);
    }

    boolean registerJob(ComputationJob job) throws ResultsRegistryException {
        String jobId = job.getJobId();
        if (completedEventsMap.containsKey(jobId)){
            throw new ResultsRegistryException(String
                    .format("Completed events map contains job key: %s", jobId));
        }
        if (jobSizes.containsKey(jobId)){
            throw new ResultsRegistryException(String
                    .format("Job sizes map contains job key: %s", jobId));
        }
        jobSizes.put(jobId, job.getJobSize());
        completedEventsMap.put(jobId, new ArrayList<>());
        log.info("New job registered, job id: {}", jobId);
        return true;
    }

    private void produceJobCompleteEvent(String jobId){
        eventsPublisher.publishJobCompletedEvent(
                new JobCompleteEvent(this, jobId, completedEventsMap.get(jobId)));
    }
}
