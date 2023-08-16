package com.mpdgr.jobcontroller.service;

import com.mpdgr.jobcontroller.domain.ComputationTask;
import com.mpdgr.jobcontroller.domain.ComputationType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Scope("prototype")
public class TaskGenerator {
    Random rand = new Random();

    ComputationTask createTask(ComputationType type) {
        return switch (type) {
            case ADDITION -> createAdditionTask();
            case DIVISION -> createDivisionTask();
            case EXPONENT -> createExponentTask();
            case MULTIPLICATION -> createMultiplicationTask();
            default -> throw new IllegalArgumentException("Illegal computation type");
        };
    }

    private ComputationTask createAdditionTask() {
        return new ComputationTask(ComputationType.ADDITION,
                rand.nextDouble(100_000),
                rand.nextDouble(100_000)
        );
    }

    private ComputationTask createDivisionTask(){
        return new ComputationTask(ComputationType.DIVISION,
                rand.nextDouble(100_000 + 10_000),
                rand.nextDouble(1_000) + Double.MIN_VALUE //TODO: min to avoid zero division
        );
    }

    private ComputationTask createExponentTask(){
        return new ComputationTask(ComputationType.EXPONENT,
                rand.nextDouble(10),
                rand.nextInt(5)
        );
    }

    private ComputationTask createMultiplicationTask(){
        return new ComputationTask(ComputationType.MULTIPLICATION,
                rand.nextDouble(10_000),
                rand.nextDouble(10_000)
        );
    }
}
