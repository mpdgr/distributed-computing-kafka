package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.taskscheduler.domain.ComputationEvent;
import com.mpdgr.taskscheduler.exceptions.ProgressReportMissingException;
import com.mpdgr.taskscheduler.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComputationEventsConsumer {
    private final TaskSchedulerService service;
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic.compute-task}"})
    public void onMessage(ConsumerRecord<String, String> record)
            throws JsonProcessingException, ProgressReportMissingException {
        log.debug("Event received: {}", record);
        ComputationEvent event = mapper.readValue(record.value(), ComputationEvent.class);
        service.scheduleTask(event);
    }
}
