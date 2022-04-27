package com.foco.mq.autoconfigure;

import com.foco.mq.extend.impl.*;
import com.foco.mq.properties.MqProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChenMing
 * @date 2021/11/5
 */
@Configuration
public class ExtendBeforeProcessorConfiguration {

    @Bean
    @ConditionalOnClass(name = "com.foco.cloud.discovery.config.DiscoveryProperties")
    @ConditionalOnMissingBean(RouteBeforeProcessor.class)
    public RouteBeforeProcessor routeBeforeProcessor() {
        return new RouteBeforeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(LoginInfoBeforeProcessor.class)
    public LoginInfoBeforeProcessor loginInfoBeforeProcessor() {
        return new LoginInfoBeforeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(HttpContextBeforeProcessor.class)
    public HttpContextBeforeProcessor httpContextBeforeProcessor() {
        return new HttpContextBeforeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionConsumeBeforeProcessor.class)
    public ExceptionConsumeBeforeProcessor exceptionConsumeBeforeProcessor() {
        return new ExceptionConsumeBeforeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(FocoContextBeforeProcessor.class)
    public FocoContextBeforeProcessor focoContextBeforeProcessor() {
        return new FocoContextBeforeProcessor();
    }

    @Bean
    @ConditionalOnProperty(prefix = MqProperties.MQ_PREFIX, value = "log")
    @ConditionalOnMissingBean(LogBeforeProcessor.class)
    public LogBeforeProcessor logBeforeProcessor() {
        return new LogBeforeProcessor();
    }

}
