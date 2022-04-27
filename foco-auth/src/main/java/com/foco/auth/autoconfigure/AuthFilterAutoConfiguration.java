package com.foco.auth.autoconfigure;

import com.foco.auth.interceptor.AuthFilter;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/14 17:48
 */
@ConditionalOnClass(name = MainClassConstant.SPRING_CLOUD_GATEWAY)
public class AuthFilterAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AuthFilter.class)
    AuthFilter authFilter(){
        return new AuthFilter();
    }
}
