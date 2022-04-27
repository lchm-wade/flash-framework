package com.foco.context.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2022/03/10 10:01
 * @since foco2.1.0
 */
public class ClassUtil {
    /**
     *
     * @param clazz
     * @return 获取当前class以及父类的所有属性
     */
    public static Field[] getAllFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
