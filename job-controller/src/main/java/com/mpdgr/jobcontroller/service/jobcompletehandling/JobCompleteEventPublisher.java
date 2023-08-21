package com.mpdgr.jobcontroller.service.jobcompletehandling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCompleteEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishJobCompletedEvent(JobCompleteEvent event) {
        log.info("Publishing job complete event, job id: {}", event.getJobId());
        applicationEventPublisher.publishEvent(event);
    }
}
