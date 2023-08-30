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
    private final WorkerProperties properties;
    private final ComputationEventProducer eventProducer;

    public ComputationEvent processEvent(ComputationEvent event)
            throws InterruptedException, TaskMismatchException, JsonProcessingException, ExecutionException {

        ComputationType type = event.getTask().getType();
        log.info("Assigning worker type: {}", type.toString().toUpperCase());
        log.info("Assigning worker delay: {} ms", properties.getComputationDelay());

        Computer computer =
                switch (type) {
                    case ADDITION -> new Adder(properties.getComputationDelay());
                    case DIVISION -> new Divider(properties.getComputationDelay());
                    case EXPONENT -> new Exponent(properties.getComputationDelay());
                    case MULTIPLICATION -> new Multiplier(properties.getComputationDelay());
                };

        //resolve task
        ComputationTask resolved = computer.resolveTask(event.getTask());

        //sign
        event.setWorkerId(properties.getWorkerId());
        log.debug("Resolved task: job: {}, task nr: {}, worker type: SUPERWORKER, worker id: {}",
                event.getJobId(), event.getTaskNr(), event.getWorkerId());

        //send to completed topic
        eventProducer.sendComputationEventSynchronous(event);
        return event;
    };
}
