package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.SuperworkerState;
import com.mpdgr.commonrepo.enumeration.SuperworkerStateType;
import com.mpdgr.taskscheduler.service.SuperWorkerMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SuperworkerStateConsumer {
    private final SuperWorkerMonitor superWorkerMonitor;
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic.supercomputer-state}"})
    public void onMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.trace("Supercomputer state recorded: {}", record);
        SuperworkerState state = mapper.readValue(record.value(), SuperworkerState.class);
        superWorkerMonitor.setIdle(state.getState() == SuperworkerStateType.IDLE);
    }
}
