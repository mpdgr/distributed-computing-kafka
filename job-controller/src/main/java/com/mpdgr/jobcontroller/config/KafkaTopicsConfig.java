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
    @Value("${spring.kafka.topic.compute-task}")
    private String compute;
    @Value("${spring.kafka.topic.completed-task}")
    private String completed;
    @Value("${spring.kafka.topic.addition}")
    private String addition;
    @Value("${spring.kafka.topic.multiplication}")
    private String multiplication;
    @Value("${spring.kafka.topic.division}")
    private String division;
    @Value("${spring.kafka.topic.exponent}")
    private String exponent;
    @Value("${spring.kafka.topic.supercomputer}")
    private String supercomputer;

    @Value("${spring.kafka.topic.supercomputer-state}")
    private String supercomputerState;

    @Bean
    public NewTopic compute(){
        log.info("Creating Kafka topic: {}", compute);
        return TopicBuilder
                .name(compute)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic completed(){
        log.info("Creating Kafka topic: {}", completed);
        return TopicBuilder
                .name(completed)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic addition(){
        log.info("Creating Kafka topic: {}", addition);
        return TopicBuilder
                .name(addition)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic multiplication(){
        log.info("Creating Kafka topic: {}", multiplication);
        return TopicBuilder
                .name(multiplication)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic division(){
        log.info("Creating Kafka topic: {}", division);
        return TopicBuilder
                .name(division)
                .partitions(3)
                .replicas(3)
                .build();
    }

    //6 partitions for exponent as 3 consumers assumed
    @Bean
    public NewTopic exponent(){
        log.info("Creating Kafka topic: {}", exponent);
        return TopicBuilder
                .name(exponent)
                .partitions(6)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic supercomputer(){
        log.info("Creating Kafka topic: {}", supercomputer);
        return TopicBuilder
                .name(supercomputer)
                .partitions(3)
                .replicas(3)
                .build();
    }

    //only one partition to keep track of message order
    @Bean
    public NewTopic supercomputerState(){
        log.info("Creating Kafka topic: {}", supercomputerState);
        return TopicBuilder
                .name(supercomputerState)
                .partitions(1)
                .replicas(3)
                .build();
    }
}
