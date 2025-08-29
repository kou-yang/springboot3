package com.conggua.common.base.util;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author ky
 * @description SpEL表达式解析工具类
 * @date 2023-12-13 16:10
 */
public class SpElUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static <T> T parseSpEl(Method method, Object[] args, String spEl, Class<T> returnClass) {
        // 解析参数名
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        // el 解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        Expression expression = parser.parseExpression(spEl);
        return expression.getValue(context, returnClass);
    }

    public static <T> T parseSpEl(Method method, Object[] args, String spEl, Object rootObject, Class<T> returnClass) {
        // 解析参数名
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        // el 解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext(rootObject);
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        Expression expression = parser.parseExpression(spEl);
        return expression.getValue(context, returnClass);
    }
}
