package com.conggua.common.web.controller;

import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.model.entity.User;
import com.conggua.common.web.sse.SseClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;

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
    public SseEmitter subscribe() {
        // 只允许以当前登录用户身份订阅，防止冒充他人
        return sseClient.createSseEmitter(UserHolder.getUserId());
    }

    @Operation(summary = "SSE 发布消息")
    @GetMapping("/push")
    public void push(String userId, String message) {
        // 仅后台用户（type=0，见 User.type 约定）可推送
        User user = UserHolder.get();
        if (Objects.isNull(user) || !Objects.equals(user.getType(), 0)) {
            throw new BusinessException(CommonErrorEnum.NO_AUTH_ERROR);
        }
        sseClient.sendMessage(userId, message);
    }

    @Operation(summary = "SSE 断开连接")
    @GetMapping(path = "disconnect")
    public void disconnect(HttpServletRequest request) {
        request.startAsync();
        sseClient.removeUser(UserHolder.getUserId());
    }
}
