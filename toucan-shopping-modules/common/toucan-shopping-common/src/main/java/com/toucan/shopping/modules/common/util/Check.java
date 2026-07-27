package com.toucan.shopping.modules.common.util;

import com.toucan.shopping.modules.common.exception.BusinessValidationException;

import java.util.Collection;
import java.util.Objects;

/**
 * 业务参数校验工具，校验失败抛出 BusinessValidationException
 */
public final class Check {
    private Check() {}

    /** 对象不为 null */
    public static void notNull(Object value, int code, String msg) {
        if (value == null) {
            throw new BusinessValidationException(code, msg);
        }
    }

    /** 字符串不为空 */
    public static void notEmpty(String value, int code, String msg) {
        if (value == null || value.isEmpty()) {
            throw new BusinessValidationException(code, msg);
        }
    }

    /** 集合不为空 */
    public static void notEmpty(Collection<?> coll, int code, String msg) {
        if (coll == null || coll.isEmpty()) {
            throw new BusinessValidationException(code, msg);
        }
    }

    /** 条件为 true 则通过 */
    public static void isTrue(boolean condition, int code, String msg) {
        if (!condition) {
            throw new BusinessValidationException(code, msg);
        }
    }

    /** 条件为 false 则通过 */
    public static void isFalse(boolean condition, int code, String msg) {
        if (condition) {
            throw new BusinessValidationException(code, msg);
        }
    }

    /** 两个值相等 */
    public static <T> void equals(T a, T b, int code, String msg) {
        if (!Objects.equals(a, b)) {
            throw new BusinessValidationException(code, msg);
        }
    }
}
