package com.mpdgr.worker.config;

import com.mpdgr.commonrepo.exception.ComputationException;
import com.mpdgr.commonrepo.exception.SystemException;
import com.mpdgr.worker.errorhandling.ComputationExceptionRecoverer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {
    ComputationExceptionRecoverer recoverer;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setConcurrency(1); //consumer is intended to work single-threaded/ blocking
        factory.setCommonErrorHandler(errorHandler());
        factory.afterPropertiesSet();
        return factory;
    }

    public DefaultErrorHandler errorHandler(){
        FixedBackOff backOff = new FixedBackOff(100L, 1);
        DefaultErrorHandler handler = new DefaultErrorHandler(backOff);
        handler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            log.debug("Delivery attempt exception: {}, record {}", ex.getMessage(), record);
        }));
        handler.addNotRetryableExceptions(SystemException.class);
        handler.addRetryableExceptions(ComputationException.class);
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
