package com.conggua.common.base.common;

import com.conggua.common.base.exception.CommonErrorEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author kouyang
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    private Integer code;
    private T data;
    private String message;

    public Result(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(Integer code, T data) {
        this(code, data, "");
    }

    public Result(CommonErrorEnum commonErrorEnum) {
        this(commonErrorEnum.getCode(), null, commonErrorEnum.getMessage());
    }

    public Result(CommonErrorEnum commonErrorEnum, T data) {
        this(commonErrorEnum.getCode(), data, commonErrorEnum.getMessage());
    }
}
