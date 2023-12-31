package com.mpdgr.worker.kafka;

import com.mpdgr.commonrepo.domain.JobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
public class SendResultBiConsumer<T extends JobEvent> implements BiConsumer<SendResult<String, String>, Throwable> {
    private final T event;
    private final String SUCCESS_MESSAGE = "Kafka producer  event sent - event: {}";
    private final String ERROR_MESSAGE = "Kafka producer sending error! jobId: {}, error: {}event: {}; ";

    @Override
    public void accept(SendResult<String, String> result, Throwable throwable) {
        if (throwable != null){
            log.error(ERROR_MESSAGE, event.getJobId(), throwable, event);
        } else {
            log.trace(SUCCESS_MESSAGE, event.getJobId());
        }
    }
}