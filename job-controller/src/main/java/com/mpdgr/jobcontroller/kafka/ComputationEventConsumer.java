package com.mpdgr.jobcontroller.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.jobcontroller.service.CompletedEventManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComputationEventConsumer {
    private final CompletedEventManager eventProcessor;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "${spring.kafka.topic.completed-task}")
    public void onMessage(ConsumerRecord<String, String> record)
            throws JsonProcessingException, ResultsRegistryException {
        log.debug("Event received: {}", record);
        ComputationEvent receivedEvent = mapper.readValue(record.value(), ComputationEvent.class);
        ComputationEvent processedEvent = eventProcessor.processCompletedEvent(receivedEvent);
        log.debug("Event consumed: {}", processedEvent);
    }
}
