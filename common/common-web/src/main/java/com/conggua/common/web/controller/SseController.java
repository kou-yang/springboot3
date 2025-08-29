package com.conggua.common.web.controller;

import com.conggua.common.web.sse.SseClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author ky
 * @description Server Send Event
 * @date 2024-04-19 21:42
 */
@Tag(name = "SSE", description = "Server Send Event")
@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseClient sseClient;

    @Operation(summary = "SSE 订阅")
    @GetMapping("/subscribe")
    public SseEmitter subscribe(String userId) {
        return sseClient.createSseEmitter(userId);
    }

    @Operation(summary = "SSE 发布消息")
    @GetMapping("/push")
    public void push(String userId, String message) {
        sseClient.sendMessage(userId, message);
    }

    @Operation(summary = "SSE 断开连接")
    @GetMapping(path = "disconnect")
    public void disconnect(String userId, HttpServletRequest request) {
        request.startAsync();
        sseClient.removeUser(userId);
    }
}
