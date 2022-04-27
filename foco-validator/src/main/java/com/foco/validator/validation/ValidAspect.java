package com.foco.validator.validation;


import cn.hutool.core.util.StrUtil;
import com.foco.context.core.SpringContextHolder;
import com.foco.model.constant.AopOrderConstants;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import com.foco.validator.custom.CustomCheck;
import com.foco.validator.custom.CustomChecker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * description: 参数校验AOP
 *
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
@Slf4j
@Aspect
@Order(AopOrderConstants.VALIDATION)
public class ValidAspect {
    /**
     * 拦截指定注解
     */
    @Pointcut("@annotation(com.foco.validator.validation.ParamValidate)")
    public void pointcutAnnotation() {
    }

    @Around("pointcutAnnotation()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        log.debug("开始参数校验，请求方法：{}", method.getDeclaringClass().getName() + "." + methodName);
        List<String> errors = null;
        try {
            //TODO 获取错误信息
            errors = getErrors(method, pjp.getArgs());
        } catch (Exception e) {
            SystemException.throwException(FocoErrorCode.PARAMS_ERROR.getCode(), "方法参数校验异常", e);
        }
        if (errors != null && errors.size() > 0) {
            String msg = StrUtil.join(",", errors.toArray());
            throw new SystemException(FocoErrorCode.VALID_ERROR.getCode(), msg);
        }
        return pjp.proceed();
    }

    /**
     * 校验参数，返回提示信息
     *
     * @param method
     * @param arguments
     * @return
     */
    public List<String> getErrors(Method method, Object[] arguments) throws Exception {
        List<String> list = new ArrayList<>();
        if (arguments != null && arguments.length > 0) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            ParamValidate validatable = method.getAnnotation(ParamValidate.class);
            Class<?>[] groups = validatable.groups();
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if(argument!=null){
                    Set<ConstraintViolation<Object>> constraintViolations;
                    if (groups.length > 0) {
                        //校验分组参数
                        constraintViolations = validator.validate(argument, groups);
                    } else {
                        constraintViolations = validator.validate(argument);
                    }
                    if (constraintViolations != null && !constraintViolations.isEmpty()) {
                        for (Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator(); iterator.hasNext(); ) {
                            list.add(iterator.next().getMessage());
                        }
                    }
                    customCheck(argument, groups, list);
                }
            }
        }
        return list;
    }

    private void customCheck(Object argument, Class<?>[] groups, List<String> list) throws Exception {
        Class<?> paramClass = argument.getClass();
        for (Field field : paramClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(CustomCheck.class)) {
                CustomCheck customCheck = field.getAnnotation(CustomCheck.class);
                Class<?>[] groupsCheck = customCheck.groups();
                if (isEqual(groups, groupsCheck)) {
                    CustomChecker customChecker = SpringContextHolder.getBean(customCheck.checkClass());
                    field.setAccessible(true);
                    boolean check = customChecker.check(field.get(argument), customCheck);
                    if (!check) {
                        list.add(customCheck.message());
                    }
                }
            }
        }
    }


    private boolean isEqual(Class<?>[] class1, Class[] class2) {
        if (class1.length == 0 && class2.length == 0) {
            return true;
        }
        for (int i = 0; i < class1.length; i++) {
            for (int j = 0; j < class2.length; j++) {
                if (class1[i].isAssignableFrom(class2[j])) {
                    return true;
                }
            }
        }
        return false;
    }
}
