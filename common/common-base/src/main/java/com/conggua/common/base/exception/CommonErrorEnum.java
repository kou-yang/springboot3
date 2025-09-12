package com.conggua.common.base.exception;

/**
 * 错误码
 *
 * @author kouyang
 */
public enum CommonErrorEnum implements ErrorEnum {

    SUCCESS(200, "ok"),
    SYSTEM_ERROR(500, "系统内部异常"),
    REQUEST_PARAM_INVALID_ERROR(700, "请求参数错误"),
    VISIT_FREQUENTLY_ERROR(701, "访问频繁，请稍后再试"),
    LOCK_LIMIT_ERROR(702, "访问频繁，请稍后再试"),
    REPEAT_SUBMIT_ERROR(703, "重复提交"),

    NOT_LOGIN_ERROR(40001, "未登录"),
    ACCOUNT_ERROR(40002, "账号错误"),
    FORBIDDEN_ERROR(40003, "账号被禁用"),
    PASSWORD_ERROR(40004, "密码错误"),
    NO_AUTH_ERROR(40005, "无权限"),
    PARAMS_ERROR(40006, "请求参数错误"),
    NOT_FOUND_ERROR(40007, "请求数据不存在"),
    OPERATION_ERROR(40008, "操作失败"),
    UN_KNOWN_SESSION_ERROR(40009, "token 过期或错误"),
    SAVE_ERROR(40010, "保存失败"),
    UPDATE_ERROR(40011, "更新失败"),
    DELETE_ERROR(40012, "删除失败"),
    DUPLICATE_ERROR(40013, "数据重复");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    CommonErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
