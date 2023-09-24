package com.mpdgr.jobcontroller.service;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.domain.ComputationTask;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComputationEventsGenerator {
    private final TaskGenerator taskGenerator;

    List<ComputationEvent> createEventList(ComputationJob job) {
        List<ComputationEvent> events = new ArrayList<>();
        events.addAll(createEvents(job.getAddition(), ComputationType.ADDITION, job.getJobId()));
        events.addAll(createEvents(job.getMultiplication(), ComputationType.MULTIPLICATION, job.getJobId()));
        events.addAll(createEvents(job.getDivision(), ComputationType.DIVISION, job.getJobId()));
        events.addAll(createEvents(job.getExponent(), ComputationType.EXPONENT, job.getJobId()));

        /* shuffle to randomize tasks type stream */
        Collections.shuffle(events);

        /* add tasks numbering */
        for (int i = 0; i < events.size(); i++) {
            events.get(i).setTaskNr(i + 1);
        }

        log.info("Created events stream - job id: {}; events count: {};", job.getJobId(), events.size());
        return events;
    }

    List<ComputationEvent> createEvents(int nrOfTasks, ComputationType taskType, String jobId) {
        List<ComputationEvent> events = new ArrayList<>();
        for (int i = 0; i < nrOfTasks; i++) {
            ComputationEvent event = new ComputationEvent(jobId);
            ComputationTask task = taskGenerator.createTask(taskType);
            event.setTask(task);
            events.add(event);
        }
        return events;
    }
}
