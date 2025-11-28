package com.conggua.common.base.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.conggua.common.base.common.Result;
import com.conggua.common.base.util.SpringContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ky
 * @description
 * @date 2023-12-12 16:29
 */
public interface ErrorEnum {

    int getCode();

    String getMessage();

    default void sendHttpError() throws IOException {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
        Result<String> result = new Result<>(this.getCode(), null, this.getMessage() + "，uri: " + request.getRequestURI());
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
