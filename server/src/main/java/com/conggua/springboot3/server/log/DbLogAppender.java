package com.conggua.springboot3.server.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.conggua.common.base.util.SpringContextUtils;

/**
 * @author: kouyang
 * @description:
 * @date: 2026-07-10 16:27
 */
public class DbLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        // Spring 容器未就绪时不处理（启动阶段日志）
        if (SpringContextUtils.getApplicationContext() == null) {
            return;
        }
        SpringContextUtils.getApplicationContext().publishEvent(new LogPersistEvent(this, event));
    }
}
