package com.mpdgr.workercomputer.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.List;
import java.util.Map;

//public class CustomKafkaConsumerFactory<K, V> extends DefaultKafkaConsumerFactory<K, V> {
//    @Autowired
//    @Qualifier("incoming-tasks-topic")
//    private String incomingTasksTopic;
//
//
//    public CustomKafkaConsumerFactory(Map<String, Object> configs) {
//        super(configs);
//    }
//
//    protected Consumer<K, V> createKafkaConsumer(Map<String, Object> configProps) {
//        Consumer<K, V> kafkaConsumer = super.createKafkaConsumer(configProps);
//        kafkaConsumer.subscribe(List.of(incomingTasksTopic));
//        return kafkaConsumer;
//    }
//}
