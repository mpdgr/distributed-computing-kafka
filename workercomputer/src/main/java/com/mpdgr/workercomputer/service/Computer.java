package com.mpdgr.workercomputer.service;

import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;

public interface Computer {
    ComputationTask resolveTask(ComputationTask task) throws TaskMismatchException;
    ComputationType getComputerType(ComputationType type);
}
