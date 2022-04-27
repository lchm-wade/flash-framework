package com.foco.mq.transactional.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.db.annotation.IgnoreLogicDelete;
import com.foco.mq.autoconfigure.MqConfiguration;
import com.foco.mq.core.producer.FocoMsgProducer;
import com.foco.mq.transactional.aspect.AfterTransactionAspect;
import com.foco.mq.transactional.core.RetryMessageScheduler;
import com.foco.mq.transactional.core.TransactionalMsgProducer;
import com.foco.mq.transactional.properties.TransactionalProperties;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

/**
 * @author ChenMing
 * @date 2021/10/15
 */
@Configuration
@ConditionalOnClass({Mapper.class, IgnoreLogicDelete.class})
@AutoConfigureBefore(MqConfiguration.class)
@EnableConfigurationProperties(TransactionalProperties.class)
public class MqTransactionalConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-mq-transactional",this.getClass());
    }
    @Bean
    @ConditionalOnMissingBean(AfterTransactionAspect.class)
    public AfterTransactionAspect focoAfterTransactionAspect(ConfigurableListableBeanFactory beanFactory) {
        return new AfterTransactionAspect(beanFactory);
    }

    @Bean
    @Primary
    public FocoMsgProducer transactionalMsgProducer(ConfigurableListableBeanFactory beanFactory) {
        return new TransactionalMsgProducer(beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean(RetryMessageScheduler.class)
    public RetryMessageScheduler retryMessageScheduler() {
        return new RetryMessageScheduler();
    }
}
