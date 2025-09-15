package com.conggua.common.redis.aop;

import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.NetworkUtils;
import com.conggua.common.base.util.SpElUtils;
import com.conggua.common.base.util.SpringContextUtils;
import com.conggua.common.redis.annotation.RateLimiter;
import com.conggua.common.redis.constant.RateLimiterConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ky
 * @description 限流 AOP
 * @date 2023-12-13 16:10
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE)
@Component
public class RateLimitAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> SLIDING_WINDOW_SCRIPT;

    static {
        SLIDING_WINDOW_SCRIPT = new DefaultRedisScript<>();
        SLIDING_WINDOW_SCRIPT.setLocation(new ClassPathResource("lua/rateLimiter.lua"));
        SLIDING_WINDOW_SCRIPT.setResultType(Long.class);
    }

    @Before("@annotation(rateLimiter)")
    public void before(JoinPoint joinPoint, RateLimiter rateLimiter) {
        // zSet 集合的 key
        String key = getKey(joinPoint, rateLimiter);
        // 生成 value 值
        String uuid = UUID.randomUUID().toString();
        // 当前时间戳（score）
        String now = String.valueOf(System.currentTimeMillis());
        // 窗口大小
        long millis = rateLimiter.timeUnit().toMillis(rateLimiter.time());
        // key 过期时间
        long seconds = rateLimiter.timeUnit().toSeconds(rateLimiter.time());
        // 窗口时间内允许最大访问次数
        int count = rateLimiter.count();
        // 执行 lua 脚本
        Long result = stringRedisTemplate.execute(
                SLIDING_WINDOW_SCRIPT,
                Arrays.asList(key, uuid, now),
                String.valueOf(millis), String.valueOf(seconds), String.valueOf(count)
        );
        if (Objects.isNull(result) || result.intValue() == RateLimiterConstant.UN_ALLOWED) {
            throw new BusinessException(CommonErrorEnum.VISIT_FREQUENTLY_ERROR);
        }
    }

    /**
     * 生成 key
     */
    private String getKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 默认方法限定名
        String prefix = StringUtils.isBlank(rateLimiter.prefixKey()) ? method.getDeclaringClass() + "#" + method.getName() + ":" : rateLimiter.prefixKey() + ":";
        String key = "";
        switch (rateLimiter.target()) {
            case IP:
                key = prefix + NetworkUtils.getIpAddress(SpringContextUtils.getHttpServletRequest());
                break;
            case EL:
                key = prefix + SpElUtils.parseSpEl(method, joinPoint.getArgs(), rateLimiter.spEl(), String.class);
                break;
            case UID:
                key = prefix + UserHolder.getUserId();
            default:
                break;
        }
        return key;
    }
}
