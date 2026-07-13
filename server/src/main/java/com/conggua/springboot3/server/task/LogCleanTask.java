package com.conggua.springboot3.server.task;

import cn.hutool.core.util.RandomUtil;
import com.conggua.springboot3.server.mapper.LogMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author kouyang
 * @description 日志清理定时任务
 * @date 2026-07-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogCleanTask {

    private final LogMapper logMapper;

    @SneakyThrows
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanExpiredLog() {
        while (true) {
            LocalDateTime thresholdTime = LocalDateTime.now().minusDays(7);
            int deletedCount = logMapper.deleteByExpiredTime(thresholdTime);
            if (deletedCount == 0) {
                break;
            }
            TimeUnit.SECONDS.sleep(RandomUtil.randomInt(5, 10));
        }
    }
}