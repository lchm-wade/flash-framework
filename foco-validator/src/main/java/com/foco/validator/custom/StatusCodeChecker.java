package com.foco.validator.custom;

import com.foco.context.common.StatusCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Description 状态码校验，对前端输入的状态码的值是否合法做校验
 * @Author lucoo
 * @Date 2021/5/21 17:23
 */
@Slf4j
public class StatusCodeChecker implements CustomChecker {
    @Override
    public boolean check(Object fieldVal, CustomCheck customCheck) {
        Class<? extends StatusCode> targetEnum = customCheck.checkTarget();
        if(!targetEnum.equals(StatusCode.class)) {
            boolean checked=check(fieldVal,targetEnum);
            if(!checked){
                log.warn("状态码校验不通过value:{}",fieldVal);
            }
            return checked;
        }
        log.warn("状态码校验不通过value:{}",fieldVal);
        return false;
    }
    private boolean check(Object fieldVal, Class<? extends StatusCode> targetEnum){
        try {
            StatusCode[] values = (StatusCode[]) targetEnum.getMethod("values").invoke(null);
            return Arrays.stream(values).anyMatch(i-> String.valueOf(i.getCode()).equals(String.valueOf(fieldVal)));
        } catch (Exception e) {
            log.error("StatusCodeChecker校验异常",e);
        }
        return false;
    }
}
