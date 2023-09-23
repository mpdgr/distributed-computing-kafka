package com.mpdgr.taskscheduler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
@Slf4j
public class ComputationEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public CompletableFuture<SendResult<String, String>> sendComputationEvent(ComputationEvent event,
                                                                              String targetTopic)
            throws JsonProcessingException {
        String key = null; //null key for round robin partition distribution
        String value = mapper.writeValueAsString(event);
        ProducerRecord<String, String> record = buildRecord(targetTopic, key, value);
        CompletableFuture<SendResult<String, String>> resultFuture = kafkaTemplate.send(record);
        return resultFuture.whenComplete(new SendResultBiConsumer<>(event));
    }

    private ProducerRecord<String, String> buildRecord(String topic, String key, String value){
        Header source = new RecordHeader("event-source", "task-scheduler".getBytes());
        Header type = new RecordHeader("event-type", "computation-event".getBytes());
        return new ProducerRecord<>(topic, null, key, value, List.of(source, type));
    }
}
