package com.mpdgr.worker.errorhandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdgr.commonrepo.domain.ComputationEvent;
import com.mpdgr.commonrepo.exception.ComputationException;
import com.mpdgr.commonrepo.exception.SystemException;
import com.mpdgr.commonrepo.exception.TaskMismatchException;
import com.mpdgr.worker.service.ComputingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
@Slf4j
public class ComputationExceptionRecoverer implements ConsumerRecordRecoverer {
    private final ObjectMapper mapper;
    private final ComputingService computingService;

    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception e) {
        log.info("Recovery: event to reprocess recorded: {}", e.getClass());
        if (e.getCause() instanceof ComputationException) {
            log.debug("ComputationException recorded");
            ConsumerRecord<String, String> invalidRecord = (ConsumerRecord<String, String>) record;
            String value = invalidRecord.value();
            ComputationEvent event = null;
            try {
                event = mapper.readValue(value, ComputationEvent.class);
                double divisor = event.getTask().getValue2();
                event.getTask().setValue2(divisor + 1);
                log.info("Recovery: event reprocessing, task nr: {}", event.getTaskNr());
                computingService.processEvent(event);
            } catch (InterruptedException | TaskMismatchException |
                     JsonProcessingException | ExecutionException ex) {
                log.error("Error reprocessing task: {}", event, ex);
                throw new SystemException(ex.getMessage());
            }
            log.info("Recovery: event reprocessed, task nr: {}", event.getTaskNr());
        } else {
            log.error("Unrecoverable exception! {}, {}", e.getClass(), e.getCause(), e);
            throw new RuntimeException();
        }
    }
}
