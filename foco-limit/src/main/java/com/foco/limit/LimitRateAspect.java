package com.foco.limit;

import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.HttpContext;
import com.foco.model.constant.AopOrderConstants;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * description----------
 *
 * @Author lucoo
 * @Date 2021/6/26 13:59
 */
@Aspect
@Slf4j
@Order(AopOrderConstants.LIMIT_RATE)
public class LimitRateAspect {
    private static final String LIMIT_KEY_PREFIX = "limit:";
    @Autowired
    private LimitProperties limitProperties;
    @Autowired
    private LimitRateSupport limitRateSupport;
    @Autowired(required = false)
    private LimitParamProvider limitParamProvider;
    /**
     *
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void limitPoint() {
    }

    @Around("limitPoint()")
    public Object limitAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] arguments = pjp.getArgs();
        Class<?> clazz = pjp.getTarget().getClass();
        String classMethodName = clazz.getName() + "#" + method.getName();
        if (!limitProperties.getWhiteList().contains(classMethodName)) {
            LimitParam limitParam;
            String key=LIMIT_KEY_PREFIX + classMethodName + ":";
            if(limitParamProvider!=null){
                limitParam=limitParamProvider.supply();
                limitParam.setKey(key+limitParam.getKey());
            }else {
                limitParam=new LimitParam();
                LoginContext loginContext = LoginContextHolder.getLoginContext(LoginContext.class);
                String userIdentification;
                if (loginContext == null) {
                    userIdentification=HttpContext.getIp();
                }else {
                    userIdentification=String.valueOf(loginContext.getUserId());
                }
                limitParam.setKey(key + userIdentification);
                if (method.isAnnotationPresent(LimitRate.class)) {
                    LimitRate limitRate = method.getAnnotation(LimitRate.class);
                    limitParam.setLimitTime(limitRate.limitTime());
                    limitParam.setLimitCount(limitRate.limitCount());
                } else {
                    limitParam.setLimitTime(limitProperties.getLimitTime());
                    limitParam.setLimitCount(limitProperties.getLimitCount());
                }
            }
            log.info("limiter method = {}, key = {}, limit = {}, expire = {}", classMethodName,
                    limitParam.getKey(), limitParam.getLimitCount(), limitParam.getLimitTime());
            boolean limited = limitRateSupport.limit(limitParam);
            if (limited) {
                SystemException.throwException(FocoErrorCode.LIMIT_RATE_ERROR);
            }
        }
        return pjp.proceed(arguments);
    }
}
