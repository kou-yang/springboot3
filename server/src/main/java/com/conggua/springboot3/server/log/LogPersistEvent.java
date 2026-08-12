package com.conggua.springboot3.server.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author: kouyang
 * @description:
 * @date: 2026-07-10 16:28
 */
@Getter
public class LogPersistEvent extends ApplicationEvent {

    private final ILoggingEvent loggingEvent;
    private final String traceId;
    private final String userId;

    public LogPersistEvent(Object source, ILoggingEvent loggingEvent, String traceId, String userId) {
        super(source);
        this.loggingEvent = loggingEvent;
        this.traceId = traceId;
        this.userId = userId;
    }
}
