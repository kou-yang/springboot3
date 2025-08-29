package com.conggua.common.base.exception;


/**
 * 自定义异常类
 *
 * @author kouyang
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(CommonErrorEnum commonErrorEnum) {
        super(commonErrorEnum.getMessage());
        this.code = commonErrorEnum.getCode();
    }

    public BusinessException(CommonErrorEnum commonErrorEnum, String message) {
        super(message);
        this.code = commonErrorEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
