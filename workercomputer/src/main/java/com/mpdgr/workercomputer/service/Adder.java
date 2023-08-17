package com.mpdgr.workercomputer.service;

import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;

public class Adder implements Computer{
    private final Long delay;

    public Adder(Long delay) {
        this.delay = delay;
    }

    @Override
    public ComputationTask resolveTask(ComputationTask task) {
        return null;
    }

    @Override
    public ComputationType getComputerType(ComputationType type) {
        return null;
    }
}
