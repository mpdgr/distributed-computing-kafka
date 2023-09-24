package com.mpdgr.superworker.service.computer;

import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Adder implements Computer {
    private final Long delay;

    public Adder(Long delay) {
        this.delay = delay;
    }

    @Override
    public ComputationTask resolveTask(ComputationTask task) throws InterruptedException, TaskMismatchException {
        double x = task.getValue1();
        double y = task.getValue2();

        /* make sure the task type fits given instance */
        if (task.getType() != this.getComputerType()) {
            throw new TaskMismatchException(String
                    .format("Worker of type: %s unable to process task of type: %s",
                            this.getComputerType().toString(), task.getType().toString()));
        }

        /* delay to pretend some serious calculations */
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(delay, TimeUnit.MILLISECONDS);

        task.setResult(x + y);
        return task;
    }

    @Override
    public ComputationType getComputerType() {
        return ComputationType.ADDITION;
    }
}
