package com.conggua.springboot3.server.model.dto.im;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

/**
 * @author kouyang
 * @since 2026-05-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserImportDTO {

    @ExcelProperty(value = "账号")
    private String account;

    @ExcelProperty(value = "密码")
    private String password;

    @ExcelProperty(value = "盐")
    private String salt;

    @ExcelProperty(value = "用户名")
    private String userName;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "性别")
    private Boolean gender;

    @ExcelProperty(value = "头像")
    private String avatar;
}