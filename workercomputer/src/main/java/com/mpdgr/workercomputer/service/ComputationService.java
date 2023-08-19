package com.mpdgr.workercomputer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;
import com.mpdgr.workercomputer.config.WorkerProperties;
import com.mpdgr.workercomputer.kafka.ComputationEventProducer;
import com.mpdgr.workercomputer.service.computer.Computer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComputationService {
    private final Computer computer;
    private final ComputationType type;
    private final String workerId = WorkerProperties.getWorkerId();
    private final ComputationEventProducer eventProducer;

    public ComputationEvent processEvent(ComputationEvent event)
            throws InterruptedException, TaskMismatchException, JsonProcessingException, ExecutionException {
        //resolve task
        ComputationTask resolved = computer.resolveTask(event.getTask());

        //sign
        event.setWorkerId(workerId);
        log.debug("Resolved task: job: {}, task nr: {}, type: {}, worker id: {}",
                event.getJobId(), event.getTaskNr(), event.getTask().getType(), event.getWorkerId());

        //send to completed topic
        eventProducer.sendComputationEventSynchronous(event);
        return event;
    };
}
