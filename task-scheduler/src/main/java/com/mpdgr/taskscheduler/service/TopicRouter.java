package com.mpdgr.taskscheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.taskscheduler.kafka.ComputationEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicRouter {
    private final ComputationEventProducer producer;

    @Value("${spring.kafka.topic-names.addition}")
    private String additionTopic;
    @Value("${spring.kafka.topic-names.multiplication}")
    private String multiplicationTopic;
    @Value("${spring.kafka.topic-names.division}")
    private String divisionTopic;
    @Value("${spring.kafka.topic-names.exponent}")
    private String exponentTopic;
    @Value("${spring.kafka.topic-names.superworker}")
    private String superworkerTopic;

    void sendToStandardWorker(ComputationEvent event)
            throws JsonProcessingException {
        ComputationType type = event.getTask().getType();
        switch (type) {
            case ADDITION -> producer.sendComputationEvent(event, additionTopic);
            case DIVISION -> producer.sendComputationEvent(event, divisionTopic);
            case EXPONENT -> producer.sendComputationEvent(event, exponentTopic);
            case MULTIPLICATION -> producer.sendComputationEvent(event, multiplicationTopic);
            default -> throw new IllegalArgumentException("Illegal computation type");
        }
    }

    void sendToSuperworker(ComputationEvent event)
            throws JsonProcessingException {
        producer.sendComputationEvent(event, superworkerTopic);
    }
}
