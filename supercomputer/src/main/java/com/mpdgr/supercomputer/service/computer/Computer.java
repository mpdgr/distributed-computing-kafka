package com.mpdgr.supercomputer.service.computer;

import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.exception.TaskMismatchException;

public interface Computer {
    ComputationTask resolveTask(ComputationTask task) throws TaskMismatchException, InterruptedException;
    ComputationType getComputerType();
}
