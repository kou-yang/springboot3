package com.conggua.common.web.sse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author ky
 * @description SSE
 * @date 2024-04-19 21:45
 */
@Slf4j
@Component
public class SseClient {

    /**
     * 当前连接数
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * 存放每个用户对应的 SseEmitter 对象（可用 Redis 替代）
     */
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     *
     * @param userId 用户id
     * @return SseEmitter
     */
    public SseEmitter createSseEmitter(String userId) {
        if (sseEmitterMap.containsKey(userId)) {
            return sseEmitterMap.get(userId);
        }
        try {
            // 设置永不过期（0L），默认过期时间 30 秒
            SseEmitter sseEmitter = new SseEmitter(0L);

            // 注册回调
            sseEmitter.onCompletion(completionCallBack(userId));
            sseEmitter.onError(errorCallBack(userId));
            sseEmitter.onTimeout(timeoutCallBack(userId));
            sseEmitterMap.put(userId, sseEmitter);

            // 数量+1
            count.getAndIncrement();

            log.info("创建Sse连接成功，userId：{}，当前连接数：{}", userId, getConnectCount());
            return sseEmitter;
        } catch (Exception e) {
            log.error("创建Sse连接异常，userId：{}", userId);
        }
        return null;
    }

    /**
     * 向指定用户发送消息
     *
     * @param userId  用户id
     * @param message 消息
     */
    public void sendMessage(String userId, String message) {
        if (sseEmitterMap.containsKey(userId)) {
            try {
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId);
            }
        }
    }

    /**
     * 广播群发消息
     *
     * @param message 消息
     */
    public void batchSendMessage(String message) {
        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("用户[{}]推送异常：{}", k, e.getMessage());
                removeUser(k);
            }
        });
    }

    /**
     * 群发消息
     *
     * @param userIdList 用户id集合
     * @param message    消息
     */
    public void batchSendMessage(List<String> userIdList, String message) {
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userIdList.forEach(userId -> sendMessage(userId, message));
        }
    }

    public void removeUser(String userId) {
        sseEmitterMap.remove(userId);
        count.getAndDecrement();
        log.info("移除用户：{}，当前连接数：{}", userId, getConnectCount());
    }

    public List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    public int getConnectCount() {
        return count.intValue();
    }

    private Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束连接：{}", userId);
            removeUser(userId);
        };
    }

    private Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("连接异常：{}", userId);
            removeUser(userId);
        };
    }
}
