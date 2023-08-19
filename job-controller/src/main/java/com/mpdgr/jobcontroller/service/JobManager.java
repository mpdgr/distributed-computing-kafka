package com.mpdgr.jobcontroller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.jobcontroller.kafka.ComputationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobManager {
    private final ComputationEventsGenerator eventsGenerator;
    private final ComputationEventProducer producer;
    private final ResultsRegistry resultsRegistry;

    public void processJob (ComputationJob job) throws JsonProcessingException, ResultsRegistryException {
        //create events list
        List<ComputationEvent> jobEvents = eventsGenerator.createEventList(job);

        //log start
        log.info("Started processing job id: {}; nr of tasks: {};", job.getJobId(), job.getJobSize());
        job.setStartTime(System.currentTimeMillis());

        //register job
        resultsRegistry.registerJob(job);

        //send events to process
        for (ComputationEvent event : jobEvents){
            log.debug("Sending event - job id: {}, task nr: {}, task type: {}",
                    event.getJobId(), event.getTaskNr(), event.getTask().getType());
            producer.sendComputationEvent(event);
        }

        //initialize event listener waiting for computation process to finish
    }
}
