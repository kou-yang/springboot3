package com.conggua.common.web.aop;

import com.conggua.common.web.annotation.DataScope;
import com.conggua.common.web.threadLocal.DataScopeContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author: kouyang
 * @description: 数据权限设置上下文 AOP
 * @date: 2025-11-05 10:53
 */
@Aspect
@Component
public class DataScopeAspect {

    @Around("@annotation(dataScope)")
    public Object doAround(ProceedingJoinPoint joinPoint, DataScope dataScope) throws Throwable {
        try {
            DataScopeContext.set(dataScope);
            return joinPoint.proceed();
        } finally {
            DataScopeContext.clear();
        }
    }
}
