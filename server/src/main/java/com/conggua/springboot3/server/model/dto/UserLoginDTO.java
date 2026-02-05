package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: kouyang
 * @description:
 * @date: 2026-02-02 11:43
 */
@Data
public class UserLoginDTO {

    @Schema(description = "登录方式：0-账号密码登录(account, password)")
    @NotNull
    private Integer type;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;
}
