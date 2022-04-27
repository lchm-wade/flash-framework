package com.foco.mq.autoconfigure;

import com.foco.mq.MsgProducer;
import com.foco.mq.core.MqServerPropertiesManager;
import com.foco.mq.core.consumer.ConsumerOwner;
import com.foco.mq.core.consumer.ResolveConsumerBeanPostProcessor;
import com.foco.mq.core.producer.FocoMsgProducer;
import com.foco.mq.core.producer.MessageTransmitterHandlerMapping;
import com.foco.mq.core.producer.TransmitterRegister;
import com.foco.mq.extend.ConsumerResolve;
import com.foco.mq.properties.MqProperties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author ChenMing
 * @date 2021/10/15
 */
@Configuration
@EnableConfigurationProperties(MqProperties.class)
public class MqConfiguration {

    @Bean
    @ConditionalOnMissingBean(FocoMsgProducer.class)
    @Primary
    public MsgProducer focoMsgProducer(ConfigurableListableBeanFactory beanFactory) {
        return new FocoMsgProducer(beanFactory);
    }

    @Bean
    public MessageTransmitterHandlerMapping messageTransmitterHandlerMapping(ConfigurableListableBeanFactory beanFactory) {
        return new MessageTransmitterHandlerMapping(beanFactory);
    }

    @Bean
    public ResolveConsumerBeanPostProcessor resolveConsumerBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        return new ResolveConsumerBeanPostProcessor(beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean(MqServerPropertiesManager.class)
    public MqServerPropertiesManager focoMqServerPropertiesManager() {
        return new MqServerPropertiesManager();
    }

    @Bean
    public ConsumerOwner consumerOwner(List<ConsumerResolve> consumerResolves, MqServerPropertiesManager manager
            , Environment environment) {
        return new ConsumerOwner(consumerResolves, manager, environment);
    }

    @Bean
    @ConditionalOnMissingBean(TransmitterRegister.class)
    public TransmitterRegister focoTransmitterRegister() {
        return new TransmitterRegister();
    }

}
