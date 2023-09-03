package com.mpdgr.superworker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.SuperworkerState;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SuperworkerStateProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.kafka.topic.superworker-state}")
    private String stateTopic;

    public CompletableFuture<SendResult<String, String>> sendSuperworkerState(SuperworkerState stateEvent)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        String key = stateEvent.getJobId(); //constant key to keep track of events order
        String value = mapper.writeValueAsString(stateEvent);
        ProducerRecord<String, String> record = buildRecord(stateTopic, key, value);
        CompletableFuture<SendResult<String, String>> resultFuture = kafkaTemplate.send(record);
        log.debug("Superworker state send: {}", stateEvent.getState());
        return resultFuture.whenComplete(new SendResultBiConsumer<>(stateEvent));
    }

    private ProducerRecord<String, String> buildRecord(String topic, String key, String value){
        Header source = new RecordHeader("event-source", "superworker".getBytes());
        Header type = new RecordHeader("event-type", "state-event".getBytes());
        return new ProducerRecord<>(topic, null, key, value, List.of(source, type));
    }
}
