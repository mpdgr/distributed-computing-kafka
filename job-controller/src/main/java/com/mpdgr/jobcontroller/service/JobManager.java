package com.mpdgr.jobcontroller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ComputationSystemException;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.jobcontroller.domain.JobCompleteSummary;
import com.mpdgr.jobcontroller.kafka.ComputationEventProducer;
import com.mpdgr.jobcontroller.service.jobcompletehandling.JobCompleteEvent;
import com.mpdgr.jobcontroller.service.jobcompletehandling.JobCompleteEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobManager {
    private final ComputationEventsGenerator eventsGenerator;
    private final ComputationEventProducer producer;
    private final ResultsRegistry resultsRegistry;
    private final JobCompleteEventHandler jobCompleteEventHandler;

    public JobCompleteSummary processJob (ComputationJob job) throws JsonProcessingException, ResultsRegistryException, ExecutionException, InterruptedException {
        //create events list
        List<ComputationEvent> jobEvents = eventsGenerator.createEventList(job);

        //log start
        log.info("Started processing job id: {}; nr of tasks: {};", job.getJobId(), job.getJobSize());
        job.setStartTime(System.currentTimeMillis());

        //register job
        resultsRegistry.registerJob(job);

        long startTime = System.currentTimeMillis();

        //send events to process
        for (ComputationEvent event : jobEvents){
            log.debug("Sending event - job id: {}, task nr: {}, task type: {}",
                    event.getJobId(), event.getTaskNr(), event.getTask().getType());
            producer.sendComputationEvent(event);
        }

        CompletableFuture<JobCompleteSummary> futureSummary = new CompletableFuture<>();

        //initialize event listener waiting for computation process to finish
        ApplicationListener<JobCompleteEvent> resultsListener = event -> {
            if (event.getJobId().equals(job.getJobId())){
                try {
                    futureSummary.complete(jobCompleteEventHandler.handleJobCompleted(job, event));
                } catch (ComputationSystemException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
        return futureSummary.get();
    }
}
