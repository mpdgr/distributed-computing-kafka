package com.mpdgr.superworker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;
import com.mpdgr.superworker.config.WorkerProperties;
import com.mpdgr.superworker.kafka.ComputationEventProducer;
import com.mpdgr.superworker.service.computer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComputationService {
    private final String workerId = WorkerProperties.getWorkerId();
    private final long workerDelay = WorkerProperties.getComputationDelay();
    private final ComputationEventProducer eventProducer;

    public ComputationEvent processEvent(ComputationEvent event)
            throws InterruptedException, TaskMismatchException, JsonProcessingException, ExecutionException {

        ComputationType type = event.getTask().getType();
        log.info("Assigning worker type: {}", type.toString().toUpperCase());
        log.info("Assigning worker delay: {} ms", workerDelay);

        Computer computer =
                switch (type) {
                    case ADDITION -> new Adder(workerDelay);
                    case DIVISION -> new Divider(workerDelay);
                    case EXPONENT -> new Exponent(workerDelay);
                    case MULTIPLICATION -> new Multiplier(workerDelay);
                };

        //resolve task
        ComputationTask resolved = computer.resolveTask(event.getTask());

        //sign
        event.setWorkerId(workerId);
        log.debug("Resolved task: job: {}, task nr: {}, worker type: SUPERWORKER, worker id: {}",
                event.getJobId(), event.getTaskNr(), event.getWorkerId());

        //send to completed topic
        eventProducer.sendComputationEventSynchronous(event);
        return event;
    };
}
