package com.mpdgr.workercomputer.config;

import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.workercomputer.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Every worker instance in the system has only one role:
 * - adder or
 * - multiplier or
 * - divider or
 * - exponent
 * and can execute only one type of arithmetic operation ( even though this module includes code for all operations)
 * Type of worker is determined basing on docker container environment variables passed in docker-compose.
 * This class holds info on the role of this specific worker instance.
 */
@Configuration
public class WorkerProperties {
    //add bean with listener config string
    //add bean with computation method (class extending general computer class)
    @Value("${instance.properties.worker.type}")
    ComputationType type;

    //delay is set for worker instance to slow down computation to test different kafka configs
    @Value("${instance.properties.worker.delay}")
    long computationDelay;

    @Bean
    public Computer assignWorkerToInstance() {
        return switch (type) {
            case ADDITION -> new Adder(computationDelay);
            case DIVISION -> new Divider(computationDelay);
            case EXPONENT -> new Exponent(computationDelay);
            case MULTIPLICATION -> new Multiplier(computationDelay);
        };
    }
}
