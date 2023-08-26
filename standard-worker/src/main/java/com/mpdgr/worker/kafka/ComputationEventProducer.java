package com.mpdgr.worker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class ComputationEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.kafka.topic.completed-task}")
    private String completedTopic;

    public ComputationEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper, String completedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
        this.completedTopic = completedTopic;
    }

    public SendResult<String, String> sendComputationEventSynchronous(ComputationEvent event)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        String key = event.getJobId();
        String value = mapper.writeValueAsString(event.getTask());
        ProducerRecord<String, String> record = buildRecord(completedTopic, key, value);
        CompletableFuture<SendResult<String, String>> resultFuture = kafkaTemplate.send(record);
        return resultFuture.get();
        //.get() to keep the producer blocking until sent todo: test
    }

    private ProducerRecord<String, String> buildRecord(String topic, String key, String value){
        Header source = new RecordHeader("event-source", "worker".getBytes());
        Header type = new RecordHeader("event-type", "computation-event".getBytes());
        return new ProducerRecord<>(topic, null, key, value, List.of(source, type));
    }
}
