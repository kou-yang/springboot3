package com.conggua.common.web.handler;


import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        log.debug("constraintViolationExceptionHandler", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getConstraintViolations().forEach(x -> errorMsg.append(x.getPropertyPath()).append(":").append(x.getMessage()).append(";"));
        String message = errorMsg.toString();
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, message.substring(0, message.length() - 1));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(":").append(x.getDefaultMessage()).append(";"));
        String message = errorMsg.toString();
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, message.substring(0, message.length() - 1));
    }

    @ExceptionHandler(value = BindException.class)
    public Result<?> bindExceptionHandler(BindException ex) {
        log.error("bindExceptionHandler", ex);
        StringBuilder errorMsg = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(":").append(x.getDefaultMessage()).append(";"));
        String message = errorMsg.toString();
        return ResultUtils.error(CommonErrorEnum.REQUEST_PARAM_INVALID_ERROR, message.substring(0, message.length() - 1));
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
}
