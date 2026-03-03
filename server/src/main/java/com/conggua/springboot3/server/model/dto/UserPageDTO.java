package com.conggua.springboot3.server.model.dto;

import com.conggua.common.web.model.request.CommonPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPageDTO extends CommonPageDTO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "手机号")
    private String phone;
}