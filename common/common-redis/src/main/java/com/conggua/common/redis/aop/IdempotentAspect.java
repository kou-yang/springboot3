package com.conggua.common.redis.aop;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.SpElUtils;
import com.conggua.common.redis.annotation.Idempotent;
import com.conggua.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-09-12 10:40
 */
@Slf4j
@Aspect
@Component
public class IdempotentAspect {

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 幂等key
        String key = SpElUtils.parseSpEl(signature.getMethod(), joinPoint.getArgs(), idempotent.key(), String.class);
        if (StringUtils.isBlank(key)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "幂等Key不能为空");
        }
        String redisKey = idempotent.prefix() + key;

        // 尝试获取锁（SETNX 原子操作）
        boolean success = RedisUtils.setIfAbsent(redisKey, "1", idempotent.expire(), idempotent.timeUnit());
        if (BooleanUtils.isFalse(success)) {
            // 重复提交，抛出异常
            throw new BusinessException(CommonErrorEnum.REPEAT_SUBMIT_ERROR, idempotent.message());
        }

        // 放行执行业务
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 业务失败时是否删除Key（允许重试）
            RedisUtils.delete(redisKey);
            throw e;
        } finally {
            RedisUtils.delete(redisKey);
        }
    }
}
