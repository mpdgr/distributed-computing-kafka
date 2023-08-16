package com.mpdgr.jobcontroller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.jobcontroller.domain.ComputationEvent;
import com.mpdgr.jobcontroller.domain.ComputationJob;
import com.mpdgr.jobcontroller.domain.ComputationTask;
import com.mpdgr.jobcontroller.domain.ComputationType;
import com.mpdgr.jobcontroller.kafka.ComputationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobProcessor {
    private final TaskGenerator taskGenerator;
    private final ComputationEventProducer producer;

    public void processJob (ComputationJob job) throws JsonProcessingException {
        //create events list
        List<ComputationEvent> jobEvents = createEventList(job);

        //log start
        log.info("Started processing job id: {}; nr of tasks: {};", job.getJobId(), job.getJobSize());
        job.setStartTime(System.currentTimeMillis());

        //send events
        for (ComputationEvent event : jobEvents){
            log.debug("Sending event - job id: {}, task nr: {}, task type: {}",
                    event.getJobId(), event.getTaskNr(), event.getTask().getType());
            producer.sendComputationEvent(event);
        }

        //initialize event listener waiting for computation process to finish
    }

    private List<ComputationEvent> createEventList(ComputationJob job){
        List<ComputationEvent> events = new ArrayList<>();
        events.addAll(createEvents(job.getAddition(), ComputationType.ADDITION, job.getJobId()));
        events.addAll(createEvents(job.getMultiplication(), ComputationType.MULTIPLICATION, job.getJobId()));
        events.addAll(createEvents(job.getDivision(), ComputationType.DIVISION, job.getJobId()));
        events.addAll(createEvents(job.getExponent(), ComputationType.EXPONENT, job.getJobId()));

        //shuffle to randomize tasks type stream
        Collections.shuffle(events);

        //add tasks numbering
        for (int i = 0; i < events.size(); i++){
            events.get(i).setTaskNr(i + 1);
        }

        log.info("Created events stream - job id: {}; events nr: {};", job.getJobId(), events.size());
        return events;
    }

    private List<ComputationEvent> createEvents(int nrOfTasks, ComputationType taskType, String jobId){
        List<ComputationEvent> events = new ArrayList<>();
        for (int i = 0; i < nrOfTasks; i++){
            ComputationEvent event = new ComputationEvent(jobId);
            ComputationTask task = taskGenerator.createTask(taskType);
            event.setTask(task);
            events.add(event);
        }
        return events;
    }
}
