package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    @NotBlank
    private String account;

    @Schema(description = "密码")
    @NotBlank
    private String password;

    @Schema(description = "用户名")
    @NotBlank
    private String userName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别")
    private Byte gender;

    @Schema(description = "头像")
    private String avatar;
}