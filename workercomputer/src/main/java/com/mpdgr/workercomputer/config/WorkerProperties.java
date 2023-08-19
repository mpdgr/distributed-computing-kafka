package com.mpdgr.workercomputer.config;

import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.workercomputer.service.computer.*;
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
    private static ComputationType type;

    //delay is set for worker instance to slow down computation to test different kafka configs
    @Value("${instance.properties.worker.delay:0}")
    private static long COMPUTATION_DELAY;

    @Value("${instance.properties.worker.id}")
    private static String WORKER_ID;

    @Bean
    public Computer assignWorkerType() {
        log.info("Assigning worker type: {}", type.toString().toUpperCase());
        log.info("Assigning worker delay: {} ms", COMPUTATION_DELAY);
        return switch (type) {
            case ADDITION -> new Adder(COMPUTATION_DELAY);
            case DIVISION -> new Divider(COMPUTATION_DELAY);
            case EXPONENT -> new Exponent(COMPUTATION_DELAY);
            case MULTIPLICATION -> new Multiplier(COMPUTATION_DELAY);
        };
    }

    public static ComputationType getType() {
        return type;
    }

    public static long getComputationDelay() {
        return COMPUTATION_DELAY;
    }

    public static String getWorkerId() {
        return WORKER_ID;
    }
}
