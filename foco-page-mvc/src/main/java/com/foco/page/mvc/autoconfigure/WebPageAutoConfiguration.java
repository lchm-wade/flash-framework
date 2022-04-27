package com.foco.page.mvc.autoconfigure;

import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.WebInterceptorOrderConstants;
import com.foco.page.mvc.PageContextTransmit;
import com.foco.page.mvc.interceptor.WebPageInterceptor;
import com.foco.page.mvc.interceptor.WebPageRequestBodyAdviceAdapter;
import com.foco.page.mvc.properties.PageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 17:06
 */
@Order(WebInterceptorOrderConstants.PAGE_INTERCEPTOR)
@Configuration
@EnableConfigurationProperties(PageProperties.class)
@ConditionalOnProperty(prefix = PageProperties.PREFIX, name = FocoConstants.ENABLED, matchIfMissing = true)
public class WebPageAutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebPageInterceptor())
                .addPathPatterns("/**");
    }
    @Bean
    WebPageRequestBodyAdviceAdapter webPageRequestBodyAdviceAdapter(){
        return new WebPageRequestBodyAdviceAdapter();
    }
    @Bean
    PageContextTransmit pageContextTransmit(){
        return new PageContextTransmit();
    }
}
