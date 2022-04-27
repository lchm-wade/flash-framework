package com.foco.mq.idempotent.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.mq.idempotent.core.ClearMessageScheduler;
import com.foco.mq.idempotent.core.IdempotentBeforeProcessorWrap;
import com.foco.mq.idempotent.core.IdempotentConsumerWrapper;
import com.foco.mq.idempotent.properties.IdempotentProperties;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author ChenMing
 * @date 2021/11/3
 */
@Configuration
@ConditionalOnClass({Mapper.class})
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-mq-idempotent",this.getClass());
    }
    @ConditionalOnMissingBean(IdempotentConsumerWrapper.class)
    @Bean
    public IdempotentConsumerWrapper idempotentConsumerWrapper() {
        return new IdempotentConsumerWrapper();
    }

    @Bean
    @ConditionalOnMissingBean(ClearMessageScheduler.class)
    public ClearMessageScheduler clearMessageScheduler() {
        return new ClearMessageScheduler();
    }

    @Bean
    public IdempotentBeforeProcessorWrap idempotentBeforeProcessorWrap(ConfigurableListableBeanFactory beanFactory) {
        return new IdempotentBeforeProcessorWrap(beanFactory);
    }
}
