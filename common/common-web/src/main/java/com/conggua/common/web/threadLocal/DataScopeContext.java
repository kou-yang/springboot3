package com.conggua.common.web.threadLocal;

import com.conggua.common.web.annotation.DataScope;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-11-05 10:56
 */
public class DataScopeContext {

    private static final ThreadLocal<DataScope> CONTEXT = new ThreadLocal<>();

    public static void set(DataScope dataScope) {
        CONTEXT.set(dataScope);
    }

    public static DataScope get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
