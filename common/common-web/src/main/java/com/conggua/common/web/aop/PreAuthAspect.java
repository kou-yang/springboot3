package com.conggua.common.web.aop;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.web.annotation.PreAuth;
import com.conggua.common.web.auth.BaseAuthFun;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author ky
 * @description 鉴权 AOP
 * @date 2024-05-27 16:24
 */
@Aspect
@Component
public class PreAuthAspect {

    @Autowired
    private ApplicationContext applicationContext;

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.conggua.common.web.annotation.PreAuth) || @within(com.conggua.common.web.annotation.PreAuth)")
    private void pointcut() {

    }

    @Around("pointcut()")
    public Object proAuth(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        PreAuth preAuth = method.getAnnotation(PreAuth.class);
        if (Objects.isNull(preAuth)) {
            preAuth = point.getTarget().getClass().getAnnotation(PreAuth.class);
        }
        String condition = preAuth.value();
        String message = preAuth.message();
        // 权限校验
        if (handleAuth(point, method, condition)) {
            return point.proceed();
        }
        if (StringUtils.isNotBlank(message)) {
            throw new BusinessException(CommonErrorEnum.NO_AUTH_ERROR, message);
        }
        throw new BusinessException(CommonErrorEnum.NO_AUTH_ERROR);
    }

    private Boolean handleAuth(ProceedingJoinPoint point, Method method, String condition) {
        if (StringUtils.isNotBlank(condition)) {
            Expression expression = EXPRESSION_PARSER.parseExpression(condition);
            StandardEvaluationContext context = getEvaluationContext(method, point.getArgs());
            return expression.getValue(context, Boolean.class);
        }
        return false;
    }

    private StandardEvaluationContext getEvaluationContext(Method method, Object[] args) {
        // 初始化spEL表达式上下文
        StandardEvaluationContext context = new StandardEvaluationContext(getEvaluationContext());
        // 设置表达式支持spring bean
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        // 解析参数名
        String[] params = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return context;
    }

    @SneakyThrows
    private BaseAuthFun getEvaluationContext() {
        Map<String, BaseAuthFun> authFunBeans = applicationContext.getBeansOfType(BaseAuthFun.class);
        if (CollectionUtils.isNotEmpty(authFunBeans.values())) {
            return authFunBeans.values().iterator().next();
        }
        return new BaseAuthFun();
    }
}
