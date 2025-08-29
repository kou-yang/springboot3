package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.util.dict.Dict;
import com.conggua.common.oss.cos.core.COSTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ky
 * @description Server Send Event
 * @date 2024-04-19 21:42
 */
@Tag(name = "SSEaaaaaaaa", description = "Server Send Event")
@RestController
@RequestMapping("/aaaa")
@RequiredArgsConstructor
public class sSseController {

    private final COSTemplate cosTemplate;

    public enum TestEnum {
        OK(100, "正常"),
        ERROR(200, "错误"),
        WARNING(300, "警告");

        private final Object code;
        private final String value;

        TestEnum(Object code, String value) {
            this.code = code;
            this.value = value;
        }

        public Object getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    @Data
    @AllArgsConstructor
    public static class EnumResponse {
        @Dict(enumClass = TestEnum.class)
        private Object code;
    }

    @Operation(summary = "SSE 订阅")
    @GetMapping("/subscribe")
    public Result<Object> subscribe(String userId) {
        return ResultUtils.success(1);
    }

    @Operation(summary = "SSE 订阅")
    @GetMapping("/subscrsssssibe")
    public Result<Object> subsssscribe() {
        return ResultUtils.success(new EnumResponse(100));
    }
}
