package com.mpdgr.worker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.exception.TaskMismatchException;
import com.mpdgr.worker.config.WorkerProperties;
import com.mpdgr.worker.kafka.ComputationEventProducer;
import com.mpdgr.worker.service.computer.Computer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComputingService {
    private final Computer computer;
    private final WorkerProperties properties;
    private final ComputationEventProducer eventProducer;

    public ComputationEvent processEvent(ComputationEvent event)
            throws InterruptedException, TaskMismatchException, JsonProcessingException,
            ExecutionException {
        /* resolve task */
        ComputationTask resolved = computer.resolveTask(event.getTask());

        /* sign */
        event.setWorkerId(properties.getWorkerId());
        event.setWorkerType(properties.getWorkerType());
        log.debug("Resolved task: job: {}, task nr: {}, worker type: {}, worker id: {}",
                event.getJobId(), event.getTaskNr(), event.getTask().getType(), event.getWorkerId());

        /* send to completed topic */
        eventProducer.sendComputationEventSynchronous(event);
        return event;
    }
}
