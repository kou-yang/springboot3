package com.conggua.common.redis.service;


import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author ky
 * @description
 * @date 2023-12-15 17:48
 */
public interface RedissonLockService {

    <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable;

    <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier);

    @FunctionalInterface
    interface SupplierThrow<T> {

        T get() throws Throwable;
    }
}
