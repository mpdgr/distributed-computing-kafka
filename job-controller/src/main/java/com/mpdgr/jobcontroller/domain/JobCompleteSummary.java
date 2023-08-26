package com.mpdgr.jobcontroller.domain;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.enumeration.ComputationType;
import com.mpdgr.commonrepo.enumeration.WorkerType;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
public class JobCompleteSummary {
    private String jobId;

    private long additionsCompleted = 0;
    private long multiplicationCompleted = 0;
    private long divisionCompleted = 0;
    private long exponentCompleted = 0;

    private long startTime;
    private long endTime;

    private long operationTimeMs;

    //nr of tasks completed by each worker
    private Map<String, Integer> allWorkersSummary = new HashMap<>();

    //tasks completed by superworker
    private Map<ComputationType, Integer> superworkerSummary = new HashMap<>();

    //init superworkersSummary hashmap
    {
        superworkerSummary.put(ComputationType.ADDITION, 0);
        superworkerSummary.put(ComputationType.MULTIPLICATION, 0);
        superworkerSummary.put(ComputationType.DIVISION, 0);
        superworkerSummary.put(ComputationType.EXPONENT, 0);
    }

    public JobCompleteSummary(@NonNull String jobId) {
        this.jobId = jobId;
    }

    public void processJobCompleteEvent(JobCompleteEvent event, long startTime) {
        this.startTime = startTime;
        endTime = event.getTimestamp();

        log.info("Preparing summary for job id: {}", event.getJobId());

        setTime();
        List<ComputationEvent> computationEvents = event.getEvents();
        computationEvents.forEach(e -> {
            registerComputationTask(e);
            registerWorkerTask(e);
            registerSuperworkerTask(e);
        });
    }

    public Set<String> workersParticipating(){
        return allWorkersSummary.keySet();
    }

    public long totalTasksCompleted(){
        return additionsCompleted + multiplicationCompleted + divisionCompleted + exponentCompleted;
    }

    private void registerComputationTask(ComputationEvent event){
        switch (event.getTask().getType()){
            case ADDITION -> additionsCompleted++;
            case MULTIPLICATION -> multiplicationCompleted++;
            case DIVISION -> divisionCompleted++;
            case EXPONENT -> exponentCompleted++;
        }
    }

    private void registerWorkerTask(ComputationEvent event){
        String workerId = event.getWorkerId();
        if (allWorkersSummary.containsKey(workerId)){
            allWorkersSummary.put(workerId, allWorkersSummary.get(workerId) + 1);
        } else {
            allWorkersSummary.put(workerId, 1);
        }
    }

    private void registerSuperworkerTask(ComputationEvent event){
        if (event.getWorkerType() == WorkerType.SUPER){
            //increment nr of tasks of given type completed by superworker
            superworkerSummary.put(event.getTask().getType(), superworkerSummary.get(event.getTask().getType()) + 1);
        }
    }

    private void setTime() {
        operationTimeMs = endTime - startTime;
    }
}
