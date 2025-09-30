package com.conggua.common.base.util;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-09-30 10:43
 */
public class CRUDUtil {

    /**
     * 校验所有条件是否均为 true，若任一为 false，则抛出指定异常
     *
     * @param exception
     * @param flags
     */
    public static void validateSuccess(RuntimeException exception, boolean... flags) {
        for (boolean flag : flags) {
            if (!flag) {
                throw exception;
            }
        }
    }

    /**
     * 校验所有保存是否成功
     *
     * @param flags
     */
    public static void validateSaveSuccess(boolean... flags) {
        validateSuccess(new BusinessException(CommonErrorEnum.SAVE_ERROR), flags);
    }

    /**
     * 校验所有更新是否成功
     *
     * @param flags
     */
    public static void validateUpdateSuccess(boolean... flags) {
        validateSuccess(new BusinessException(CommonErrorEnum.UPDATE_ERROR), flags);
    }

    /**
     * 校验所有删除是否成功
     *
     * @param flags
     */
    public static void validateDeleteSuccess(boolean... flags) {
        validateSuccess(new BusinessException(CommonErrorEnum.DELETE_ERROR), flags);
    }
}
