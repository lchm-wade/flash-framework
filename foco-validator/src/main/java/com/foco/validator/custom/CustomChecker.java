package com.foco.validator.custom;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/10 16:54
 **/
public interface CustomChecker {
    /**
     *
     * @param fieldVal 当前字段值
     * @return 返回是否校验成功
     */
    boolean check(Object fieldVal, CustomCheck customCheck);
}
