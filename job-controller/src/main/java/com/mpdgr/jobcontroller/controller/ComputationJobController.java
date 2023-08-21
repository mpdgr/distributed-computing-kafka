package com.mpdgr.jobcontroller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mpdgr.commonrepo.domain.ComputationJob;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import com.mpdgr.jobcontroller.service.JobManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ComputationJobController {
    private final JobManager jobManager;

    @PostMapping(path = "/compute")
    public ResponseEntity<ComputationJob> requestComputation(@RequestBody @Valid ComputationJob job) throws JsonProcessingException, ResultsRegistryException {
        log.info("Controller processing job started - {}", job);
        jobManager.processJob(job);
        String success = "job accepted";
        return ResponseEntity.ok(job);
    }
}
