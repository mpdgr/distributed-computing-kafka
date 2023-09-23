package com.mpdgr.jobcontroller.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class KafkaTopicsConfig {
    @Value("${spring.kafka.topic-names.compute-task}")
    private String compute;
    @Value("${spring.kafka.topic-names.completed-task}")
    private String completed;

    @Value("${spring.kafka.topic-names.addition}")
    private String addition;
    @Value("${spring.kafka.topic-names.multiplication}")
    private String multiplication;
    @Value("${spring.kafka.topic-names.division}")
    private String division;
    @Value("${spring.kafka.topic-names.exponent}")
    private String exponent;

    @Value("${spring.kafka.topic-names.superworker}")
    private String superworker;
    @Value("${spring.kafka.topic-names.superworker-state}")
    private String superworkerState;

    @Bean
    public NewTopic compute(){
        NewTopic topic = TopicBuilder
                .name(compute)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    @Bean
    public NewTopic completed(){
        NewTopic topic = TopicBuilder
                .name(completed)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    @Bean
    public NewTopic addition(){
        NewTopic topic = TopicBuilder
                .name(addition)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    @Bean
    public NewTopic multiplication(){
        NewTopic topic = TopicBuilder
                .name(multiplication)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    @Bean
    public NewTopic division(){
        NewTopic topic = TopicBuilder
                .name(division)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    //6 partitions for exponent as 3 consumers assumed
    @Bean
    public NewTopic exponent(){
        NewTopic topic = TopicBuilder
                .name(exponent)
                .partitions(6)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    @Bean
    public NewTopic superworker(){
        NewTopic topic = TopicBuilder
                .name(superworker)
                .partitions(3)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }

    //only one partition to keep track of message order
    @Bean
    public NewTopic superworkerState(){
        NewTopic topic = TopicBuilder
                .name(superworkerState)
                .partitions(1)
                .replicas(3)
                .build();
        log.info("Creating Kafka topic: {}, nr of partitions: {}, replication factor: {}",
                topic.name(), topic.numPartitions(), topic.replicationFactor());
        return topic;
    }
}
