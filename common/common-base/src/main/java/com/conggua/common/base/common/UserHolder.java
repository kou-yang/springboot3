package com.conggua.common.base.common;

import com.conggua.common.base.model.entity.User;

import java.util.Optional;

/**
 * @author ky
 * @description
 * @date 2023-12-13 19:23
 */
public class UserHolder {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void set(User user) {
        threadLocal.set(user);
    }

    public static User get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static String getUserId() {
        return Optional.ofNullable(get()).map(User::getId).orElse(null);
    }

    public static String getUserName() {
        return Optional.ofNullable(get()).map(User::getUserName).orElse(null);
    }
}
