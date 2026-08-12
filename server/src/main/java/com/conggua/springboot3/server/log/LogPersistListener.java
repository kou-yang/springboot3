package com.conggua.springboot3.server.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import com.conggua.springboot3.server.model.entity.Log;
import com.conggua.springboot3.server.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author: kouyang
 * @description:
 * @date: 2026-07-10 16:28
 */
@Component
@RequiredArgsConstructor
public class LogPersistListener {

    private final LogService logService;

    @Async("logAsyncExecutor")
    @EventListener
    public void onLogEvent(LogPersistEvent event) {
        ILoggingEvent logEvent = event.getLoggingEvent();
        Log log = new Log();
        String traceId = event.getTraceId();
        log.setTraceId(traceId);
        log.setLevel(logEvent.getLevel().toString());
        log.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(logEvent.getTimeStamp()), ZoneId.systemDefault()));
        // 日志内容
        String message = logEvent.getFormattedMessage();
        IThrowableProxy throwableProxy = logEvent.getThrowableProxy();
        // 将堆栈信息追加到 message 后面
        if (throwableProxy != null) {
            String stackTrace = ThrowableProxyUtil.asString(throwableProxy);
            message = message + "\n" + stackTrace;
            if (message.length() > 60000) {
                message = message.substring(0, 60000);
            }
        }
        log.setContent(message);
        log.setUserId(event.getUserId());
        logService.save(log);
    }
}
