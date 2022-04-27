package com.foco.mq.core.producer;

import com.foco.context.asserts.Assert;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.extend.AbstractMessageTransmitter;
import com.foco.mq.extend.AbstractMqServerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/12 14:37
 * @since foco2.3.0
 */
public class TransmitterRegister implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

    private BeanDefinitionRegistry registry;

    private ConfigurableListableBeanFactory beanFactory;

    /**
     * key：{@link AbstractMessageTransmitter#AbstractMessageTransmitter(AbstractMqServerProperties)}的参数全限定名
     */
    private static Map<String, Constructor<? extends AbstractMessageTransmitter>> transmitterMap = new ConcurrentHashMap<>(8);

    public AbstractMessageTransmitter register(AbstractMqServerProperties properties) {
        String beanName = wrapTransmitterBeanName(properties);
        synchronized (TransmitterRegister.class) {
            if (!registry.isBeanNameInUse(properties.getServerId())) {
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition(TransmitterFactoryBean.class);
                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
                argumentValues.addIndexedArgumentValue(0, properties);
                beanDefinition.setConstructorArgumentValues(argumentValues);
                beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
            }
        }
        return (AbstractMessageTransmitter) beanFactory.getBean(beanName);
    }

    protected String wrapTransmitterBeanName(AbstractMqServerProperties properties) {
        return properties.getClass().getName() + MqConstant.SEPARATOR + properties.getServerId();
    }

    public static Constructor<? extends AbstractMessageTransmitter> getConstructor(String className) {
        return transmitterMap.get(className);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractMessageTransmitter) {
            AbstractMessageTransmitter transmitter = (AbstractMessageTransmitter) bean;
            Constructor<? extends AbstractMessageTransmitter> constructor = null;
            Constructor<?>[] constructors = transmitter.getClass().getConstructors();
            for (Constructor<?> c : constructors) {
                Parameter[] parameters = c.getParameters();
                if (parameters.length == 1 && AbstractMqServerProperties.class.isAssignableFrom(parameters[0].getType())) {
                    constructor = (Constructor<? extends AbstractMessageTransmitter>) c;
                }
            }
            Assert.that(constructor).isNotNull("子类扩展" + AbstractMessageTransmitter.class.getName()
                    + "需要实现一个参数为：" + AbstractMqServerProperties.class + "或其子类的构造方法。");
            transmitterMap.put(constructor.getParameters()[0].getType().getName(), constructor);
        }
        return bean;
    }
}
