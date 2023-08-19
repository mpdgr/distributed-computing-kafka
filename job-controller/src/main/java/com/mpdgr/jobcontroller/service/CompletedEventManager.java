package com.mpdgr.jobcontroller.service;

import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.ResultsRegistryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompletedEventManager {
    private final ResultsRegistry resultsRegistry;

    public ComputationEvent processCompletedEvent(ComputationEvent event) throws ResultsRegistryException {
        resultsRegistry.addCompletedEvent(event);
        log.debug("Processing completed event: {}", event);
        return event;
    }
}
