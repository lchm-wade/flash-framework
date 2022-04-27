package com.foco.context.asserts;


import com.foco.context.asserts.extensions.*;

import java.util.Collection;

/**
 * 断言工具类
 * 适合针对同一个对象多次断言
 */
public class Assert {

    public static AssertObject that(Object param) {
        return new AssertObject(param);
    }

    public static AssertCollection that(Collection<?> param) {
        return new AssertCollection(param);
    }

    public static AssertBoolean that(Boolean param) {
        return new AssertBoolean(param);
    }

    public static AssertNumber that(Number param) {
        return new AssertNumber(param);
    }

    public static AssertString that(String param) {
        return new AssertString(param);
    }
}
