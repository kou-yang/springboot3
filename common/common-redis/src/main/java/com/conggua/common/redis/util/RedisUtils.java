package com.conggua.common.redis.util;

import com.conggua.common.base.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ky
 * @description Redis工具类
 * @date 2023-12-19 22:30
 */
@Slf4j
public class RedisUtils {

    private static final String CACHE_KEY_SEPARATOR = ".";

    private static RedisTemplate redisTemplate;

    static {
        RedisUtils.redisTemplate = SpringContextUtils.getBean("redisTemplate", RedisTemplate.class);
    }

    /**
     * 构建key
     *
     * @param strObjs
     * @return
     */
    public static String buildKey(String... strObjs) {
        return Stream.of(strObjs).collect(Collectors.joining(CACHE_KEY_SEPARATOR));
    }

    /**
     * 根据指定的key删除
     *
     * @param key
     * @return
     */
    public static Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 根据指定的key集合删除
     *
     * @param keyList
     * @return
     */
    public static Long delete(List<String> keyList) {
        return redisTemplate.delete(keyList);
    }

    /**
     * 设置指定的key的过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取指定的key的过期时间
     *
     * @param key
     * @param unit
     * @return 0：永不过期
     */
    public static Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 查询key是否存在
     *
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 扫描key
     * @param pattern 正则表达式
     * @param count 匹配的数量
     * @return
     */
    public static List<String> scan(String pattern, long count) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(count).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(new String(cursor.next()));
        }
        RedisConnectionUtils.releaseConnection(rc, factory);
        return result;
    }

    /* =========================== String start =========================== */
    /**
     * 设置指定key的值
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置指定key的值和key的过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public static void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 仅当key不存在时，设置key的值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public static boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 获取指定key的值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取指定key的值
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据keys批量获取值
     *
     * @param keys
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> multiGet(Collection<String> keys, Class<T> clazz) {
        List<Object> list = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * 将给定key的值设为value，并返回key的旧值
     *
     * @param key   键
     * @param value 新值
     * @param clazz 旧值的字节码
     * @param <T>   旧值的类型
     * @return 旧值
     */
    public static <T> T getAndSet(String key, Object value, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 获取指定key的值并删除key
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getAndDelete(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().getAndDelete(key);
    }

    /**
     * 获取指定key的值并设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getAndExpire(String key, long timeout, TimeUnit unit, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().getAndExpire(key, timeout, unit);
    }
    /* =========================== String end =========================== */


    /* =========================== List start =========================== */
    public static void setList(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    public static <T> T indexList(String key, long index, Class<T> clazz) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    public static Long indexOfList(String key, Object value) {
        return redisTemplate.opsForList().indexOf(key, value);
    }
    /* =========================== List end =========================== */


    /* =========================== Hash start =========================== */
    /**
     * 添加hash
     * @param key
     * @param hashKey
     * @param value
     */
    public static void putHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取hash
     * @param key
     * @param hashKey
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getHash(String key, String hashKey, Class<T> clazz) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取hash
     * @param key
     * @param hashKey
     * @return
     */
    public static Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
    /* =========================== Hash end =========================== */


    /* =========================== Set start =========================== */
    /* =========================== Set end =========================== */


    /* =========================== ZSet start =========================== */
    /* =========================== ZSet end =========================== */


    /* =========================== Bitmap start =========================== */
    /* =========================== Bitmap end =========================== */
}
