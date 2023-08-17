package com.mpdgr.workercomputer.service;

import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;

public class Multiplier implements Computer{
    private final Long delay;

    public Multiplier(Long delay) {
        this.delay = delay;
    }

    @Override
    public ComputationTask resolveTask(ComputationTask task) throws TaskMismatchException {
        return null;
    }

    @Override
    public ComputationType getComputerType(ComputationType type) {
        return null;
    }
}
