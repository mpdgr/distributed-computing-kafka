package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.SupercomputerState;
import com.mpdgr.commonrepo.enumeration.SupercomputerStateType;
import com.mpdgr.taskscheduler.service.SuperComputerMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SupercomputerStateConsumer {
    private final SuperComputerMonitor superComputerMonitor;
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic.supercomputer-state}"})
    public void onMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.trace("Supercomputer state recorded: {}", record);
        SupercomputerState state = mapper.readValue(record.value(), SupercomputerState.class);
        superComputerMonitor.setIdle(state.getState() == SupercomputerStateType.IDLE);
    }
}
