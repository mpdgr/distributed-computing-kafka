package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.taskscheduler.domain.ComputationEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComputationEventsConsumer {
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic.compute-task}"})
    public void onMessage(ConsumerRecord<String, String> record){
        log.debug("Event received: {}", record);
    }
}
