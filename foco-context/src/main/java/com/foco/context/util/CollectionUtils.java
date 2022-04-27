package com.foco.context.util;

import java.util.Collection;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/03 14:51
 * @since foco2.1.0
 */
public class CollectionUtils {
    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return 空返回true, 非空为false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否非空
     *
     * @param collection 集合
     * @return 非空返回true, 空为false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
