package com.conggua.common.base.common;

import com.conggua.common.base.exception.CommonErrorEnum;

/**
 * 返回工具类
 *
 * @author kouyang
 */
public class ResultUtils {

    /**
     * 成功
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return new Result<>(CommonErrorEnum.SUCCESS);
    }

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(CommonErrorEnum.SUCCESS, data);
    }

    /**
     * 失败
     * @param commonErrorEnum
     * @return
     */
    public static <T> Result<T> error(CommonErrorEnum commonErrorEnum) {
        return new Result<>(commonErrorEnum);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @return
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, null, message);
    }

    /**
     * 失败
     * @param commonErrorEnum
     * @return
     */
    public static <T> Result<T> error(CommonErrorEnum commonErrorEnum, String message) {
        return new Result<>(commonErrorEnum.getCode(), null, message);
    }

    public static Result<?> ofSuccess(boolean success, CommonErrorEnum errorEnum) {
        return success ? success() : error(errorEnum);
    }
}
