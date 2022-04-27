package com.foco.page.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.DbInterceptorOrderConstants;
import com.foco.model.constant.FocoConstants;
import com.foco.page.interceptor.DbPageInterceptor;
import com.foco.page.mvc.properties.PageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 16:57
 */
@EnableConfigurationProperties(PageProperties.class)
@ConditionalOnProperty(prefix = PageProperties.PREFIX, name = FocoConstants.ENABLED, matchIfMissing = true)
public class PageAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-page",this.getClass());
    }

    @Order(DbInterceptorOrderConstants.pageInterceptor)
    @Bean
    DbPageInterceptor pageInterceptor(PageProperties pageProperties) {
        return new DbPageInterceptor(pageProperties);
    }

}
