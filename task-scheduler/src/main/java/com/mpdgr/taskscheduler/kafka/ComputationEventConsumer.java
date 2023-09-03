package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.ProgressReportMissingException;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.taskscheduler.service.CompletedTaskManager;
import com.mpdgr.taskscheduler.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComputationEventConsumer {
    private final TaskSchedulerService service;
    private final CompletedTaskManager completedTaskManager;
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic.compute-task}"})
    public void onComputeMessage(ConsumerRecord<String, String> record)
            throws JsonProcessingException, ProgressReportMissingException {
        log.debug("Event received from compute task topic, id: {}", record.key());
        ComputationEvent event = mapper.readValue(record.value(), ComputationEvent.class);
        service.scheduleTask(event);
    }

    @KafkaListener(topics = "${spring.kafka.topic.completed-task}")
    public void onCompletedMessage(ConsumerRecord<String, String> record)
            throws JsonProcessingException, ProgressReportMissingException {
        log.debug("Event received from completed task topic, id: {}", record.key());
        ComputationEvent event = mapper.readValue(record.value(), ComputationEvent.class);
        completedTaskManager.registerCompletedTask(event);
    }
}
