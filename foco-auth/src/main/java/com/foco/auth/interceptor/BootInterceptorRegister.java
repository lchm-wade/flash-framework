package com.foco.auth.interceptor;
import com.foco.model.constant.WebInterceptorOrderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/14 17:50
 */
@Configuration
@Order(WebInterceptorOrderConstants.AUTH_INTERCEPTOR)
public class BootInterceptorRegister implements WebMvcConfigurer {
    @Bean
    public BootLoginContextInterceptor bootLoginContextInterceptor() {
        return new BootLoginContextInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bootLoginContextInterceptor())
                .addPathPatterns("/**");
    }
}
