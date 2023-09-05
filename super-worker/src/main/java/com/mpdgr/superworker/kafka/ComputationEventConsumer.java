package com.mpdgr.superworker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.TaskMismatchException;
import com.mpdgr.superworker.service.ComputationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComputationEventConsumer {
    private final ObjectMapper mapper;
    private final ComputationService computationService;

    @KafkaListener(topics = {"${instance.properties.worker.reads-topic}"})
    public void onMessage(ConsumerRecord<String, String> record)
            throws JsonProcessingException, TaskMismatchException, InterruptedException, ExecutionException {
        log.debug("Event received: {}", record.value());
        ComputationEvent receivedEvent = mapper.readValue(record.value(), ComputationEvent.class);
        ComputationEvent processedEvent = computationService.processEvent(receivedEvent);
        log.debug("Event processed, task nr: {}", processedEvent.getTaskNr());
    }
}
