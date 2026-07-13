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

    public LogPersistEvent(Object source, ILoggingEvent loggingEvent) {
        super(source);
        this.loggingEvent = loggingEvent;
    }
}
