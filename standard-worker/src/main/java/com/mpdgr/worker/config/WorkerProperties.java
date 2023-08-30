package com.mpdgr.worker.config;

import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.worker.service.computer.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Every worker instance in the system has only one role:
 * - adder or
 * - multiplier or
 * - divider or
 * - exponent
 * and can execute one type of arithmetic operation (even though this module includes code for all operations)
 * Type of worker is determined basing on docker container environment variables passed in docker-compose.
 * This class holds info on the role of this specific worker instance.
 */

@Configuration
@Slf4j
@Getter
public class WorkerProperties {
    @Value("${instance.properties.worker.type}")
    private ComputationType type;

    /* delay is set for worker instance to slow down computation to test different kafka configs */
    @Value("${instance.properties.worker.delay:0}")
    private long computationDelay;

    @Value("${instance.properties.worker.id}")
    private String workerId;

    /* basing on properties certain type of worker is instantiated */
    @Bean
    public Computer assignWorkerType() {
        log.info("Assigning worker type: {}", type.toString().toUpperCase());
        log.info("Assigning worker delay: {} ms", computationDelay);
        return switch (type) {
            case ADDITION -> new Adder(computationDelay);
            case DIVISION -> new Divider(computationDelay);
            case EXPONENT -> new Exponent(computationDelay);
            case MULTIPLICATION -> new Multiplier(computationDelay);
        };
    }

    public String getWorkerId() {
        return workerId;
    }
}
