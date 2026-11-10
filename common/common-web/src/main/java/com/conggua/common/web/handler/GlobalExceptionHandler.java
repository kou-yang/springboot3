package com.conggua.common.web.handler;


import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 全局异常处理器
 *
 * @author kouyang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<?> constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException ex) {
        log.error("constraintViolationExceptionHandler", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getConstraintViolations().forEach(x -> errorMsg.append(x.getPropertyPath()).append(":").append(x.getMessage()).append(";"));
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, stripTrailingSeparator(errorMsg));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(":").append(x.getDefaultMessage()).append(";"));
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, stripTrailingSeparator(errorMsg));
    }

    @ExceptionHandler(value = BindException.class)
    public Result<?> bindExceptionHandler(BindException ex) {
        log.error("bindExceptionHandler", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(":").append(x.getDefaultMessage()).append(";"));
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, stripTrailingSeparator(errorMsg));
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public Result<?> undeclaredThrowableExceptionHandler(UndeclaredThrowableException ex) {
        log.error("undeclaredThrowableException", ex);
        Throwable cause = ex.getCause();
        if (cause != null) {
            return ResultUtils.error(CommonErrorEnum.SYSTEM_ERROR, cause.getMessage());
        }
        return ResultUtils.error(CommonErrorEnum.SYSTEM_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public Result<?> duplicateKeyExceptionHandler(DuplicateKeyException ex) {
        log.error("duplicateKeyExceptionHandler", ex);
        return ResultUtils.error(CommonErrorEnum.DUPLICATE_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException ex) {
        log.error("businessException: " + ex.getMessage(), ex);
        return ResultUtils.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception ex) {
        log.error("exception", ex);
        return ResultUtils.error(CommonErrorEnum.SYSTEM_ERROR, ex.getMessage());
    }

    /**
     * 去掉拼接错误信息时末尾多余的分隔符，空集合时返回空串，避免 substring 越界
     */
    private String stripTrailingSeparator(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
