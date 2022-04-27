package com.foco.auth.autoconfigure;

import com.foco.auth.interceptor.BootInterceptorRegister;
import com.foco.context.annotation.ConditionalOnMissingCloud;
import com.foco.model.constant.MainClassConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
/**
 * @author ChenMing
 * @date 2021/10/8
 */
@Slf4j
@ConditionalOnClass(name = MainClassConstant.SPRING_WEB_MVC)
@ConditionalOnMissingCloud
@Import(BootInterceptorRegister.class)
public class AuthInterceptorConfiguration{

}
