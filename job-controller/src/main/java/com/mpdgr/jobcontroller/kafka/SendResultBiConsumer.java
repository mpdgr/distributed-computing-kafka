package com.mpdgr.jobcontroller.kafka;

import com.mpdgr.commonrepo.domain.JobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
public class SendResultBiConsumer<T extends JobEvent> implements BiConsumer<SendResult<String, String>, Throwable> {
    private final T event;
    private static final String SUCCESS_MESSAGE = "Kafka producer  event sent - job: {}";
    private static final String ERROR_MESSAGE = "Kafka producer sending error! jobId: {}, error: {}event: {}; ";

    @Override
    public void accept(SendResult<String, String> result, Throwable throwable) {
        if (throwable != null){
            log.error(ERROR_MESSAGE, event.getJobId(), throwable, event);
            //todo: implement error handling
        } else {
            log.trace(SUCCESS_MESSAGE, event.getJobId());
        }
    }
}