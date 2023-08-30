package com.mpdgr.superworker.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Superworker can do any of operations depending on task assignment so it can support other instances of:
 * - adder or
 * - multiplier or
 * - divider or
 * - exponent
 * Like other workers superworker is intended to work single-threaded working on one event at a time
 */

@Configuration
@Slf4j
@Getter
public class WorkerProperties {

    //delay is set for worker instance to slow down computation to test different kafka configs
    @Value("${instance.properties.worker.delay:0}")
    private long computationDelay;

    @Value("${instance.properties.worker.id}")
    private String workerId;

    public long getComputationDelay() {
        return computationDelay;
    }

    public String getWorkerId() {
        return workerId;
    }
}
