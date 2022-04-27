/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.foco.syslog.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.HttpContext;
import com.foco.model.annotation.SysLog;
import com.foco.model.annotation.SysLogIgnore;
import com.foco.model.constant.AopOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 系统日志，切面处理类
 *
 * @Author lucoo
 * @Date 2021/6/27 17:55
 */
@Aspect
@Order(AopOrderConstants.SYS_LOG)
@Slf4j
public class SysLogAspect {
    @Autowired
    private SysLogThread sysLogThread;

    @Pointcut("@annotation(com.foco.model.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result;
        String errorMsg = "";
        try {
            result = point.proceed();
            //执行时长(毫秒)
        } catch (Throwable throwable) {
            errorMsg = getStackTrace(throwable);
            throw throwable;
        } finally {
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveSysLog(point, time, errorMsg);
        }
        return result;
    }

    public static String getStackTrace(Throwable throwable) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
        }
        return "";
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, String errorMsg) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            LogParam logParam = new LogParam();
            SysLog syslog = method.getAnnotation(SysLog.class);
            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            if (StrUtil.isNotBlank(syslog.value())) {
                //注解上的描述
                logParam.setOperation(syslog.value());
            } else {
                logParam.setOperation(methodName);
            }
            logParam.setMethod(className + "." + methodName + "()");
            logParam.setMethodMd5(SecureUtil.md5(logParam.getMethod()));
            logParam.setErrorMsg(errorMsg);
            if (StrUtil.isNotBlank(errorMsg)) {
                logParam.setStatus(false);
            } else {
                logParam.setStatus(true);
            }
            //请求的参数
            Object[] args = joinPoint.getArgs();
            String params;
            if (syslog.ignoreField()) {
                SimplePropertyPreFilter propertyPreFilter = new SimplePropertyPreFilter();
                Set<String> excludes = propertyPreFilter.getExcludes();
                for (Object o : args) {
                    Field[] fields = o.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(SysLogIgnore.class)) {
                            String value = field.getAnnotation(SysLogIgnore.class).value();
                            if(StrUtil.isBlank(value)){
                                value=field.getName();
                            }
                            excludes.add(value);
                        }
                    }
                }
                params = JSON.toJSONString(args, propertyPreFilter);
            } else {
                params = JSON.toJSONString(args);
            }
            logParam.setParams(params);
            //设置IP地址
            logParam.setIp(HttpContext.getIp());
            //用户名
            logParam.setUserName(LoginContextHolder.currentUserName());
            logParam.setUserId(LoginContextHolder.currentUserId());
            logParam.setTime(time);
            sysLogThread.saveSysLog(logParam);
        } catch (Exception e) {
            log.error("系统日志保存失败",e);
        }
    }
}
