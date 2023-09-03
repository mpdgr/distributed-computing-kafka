package com.mpdgr.taskscheduler.service;

import com.mpdgr.commonrepo.enumeration.ComputationType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ProgressReport {
    private final String jobId;

    //scheduled tasks
    private int additionsScheduled = 0;
    private int multiplicationsScheduled = 0;
    private int divisionsScheduled = 0;
    private int exponentsScheduled = 0;

    //completed tasks
    private int additionsCompleted = 0;
    private int multiplicationsCompleted = 0;
    private int divisionsCompleted = 0;
    private int exponentsCompleted = 0;

    //add scheduled task

    int registerScheduledAddition() {
        return additionsScheduled++;
    }

    int registerScheduledMultiplication() {
        return multiplicationsScheduled++;
    }

    int registerScheduledDivision() {
        return divisionsScheduled++;
    }

    int registerScheduledExponent() {
        return exponentsScheduled++;
    }

    //add completed task

    int registerCompletedAddition() {
        return additionsCompleted++;
    }

    int registerCompletedMultiplication() {
        return multiplicationsCompleted++;
    }

    int registerCompletedDivision() {
        return divisionsCompleted++;
    }

    int registerCompletedExponent() {
        return exponentsCompleted++;
    }

    int getMaxLagValue() {
        int maxLagValue = Collections.max(
                List.of(additionLag(), multiplicationLag(), divisionLag(), exponentLag()),
                (x, y) -> x - y);  //todo: test
        log.debug("Max lagging value: {}", maxLagValue);
        return maxLagValue;
    }

    ComputationType getMaxLaggingOperation() {
        Map<Integer, ComputationType> maxLag = new HashMap<>();
        maxLag.put(additionLag(), ComputationType.ADDITION);
        maxLag.put(multiplicationLag(), ComputationType.MULTIPLICATION);
        maxLag.put(divisionLag(), ComputationType.DIVISION);
        maxLag.put(exponentLag(), ComputationType.EXPONENT);

        Integer max = maxLag.keySet().stream()
                .max((x, y) -> x - y)
                .get(); //todo: write test

        ComputationType maxLaggingType= maxLag.get(max);
        log.debug("Max lagging type recorded: {}, lag size {}, job id: {}",
                maxLaggingType.name(), max, jobId);

        return maxLaggingType;
    }

    private int additionLag() {
        int lag = additionsScheduled - additionsCompleted;
        log.trace("Addition lag: {}", lag);
        return lag;
    }

    private int multiplicationLag() {
        int lag = multiplicationsScheduled - multiplicationsCompleted;
        log.trace("Multiplication lag: {}", lag);
        return lag;
    }

    private int divisionLag() {
        int lag = divisionsScheduled - divisionsCompleted;
        log.trace("Division lag: {}", lag);
        return lag;
    }

    private int exponentLag() {
        int lag = exponentsScheduled - exponentsCompleted;
        log.trace("Exponent lag: {}", lag);
        return lag;
    }
}
