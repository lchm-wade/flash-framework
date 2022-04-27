package com.foco.mq.core.producer;

import com.foco.context.asserts.Assert;
import com.foco.mq.extend.AbstractMessageTransmitter;
import com.foco.mq.extend.AbstractMqServerProperties;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationTargetException;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/12 14:09
 * @see TransmitterRegister
 * @since foco2.3.0
 */
public class TransmitterFactoryBean implements FactoryBean<AbstractMessageTransmitter> {

    private final AbstractMqServerProperties properties;

    public TransmitterFactoryBean(AbstractMqServerProperties properties) {
        Assert.that(properties).isNotNull("存在空的配置类，创建Transmitter失败");
        this.properties = properties;
    }

    @Override
    public AbstractMessageTransmitter getObject() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return TransmitterRegister.getConstructor(properties.getClass().getName()).newInstance(properties);
    }

    @Override
    public Class<?> getObjectType() {
        return AbstractMessageTransmitter.class;
    }
}
