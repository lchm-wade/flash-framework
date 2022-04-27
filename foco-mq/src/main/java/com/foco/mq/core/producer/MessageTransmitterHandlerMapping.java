package com.foco.mq.core.producer;

import com.foco.context.asserts.Assert;
import com.foco.mq.core.MqServerPropertiesManager;
import com.foco.mq.exception.MessagingException;
import com.foco.mq.extend.AbstractMessageTransmitter;
import com.foco.mq.extend.AbstractMqServerProperties;
import com.foco.mq.extend.MessageTransmitter;
import com.foco.mq.model.Msg;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息{@link Msg}或者其子类与{@link MessageTransmitter}映射处理器
 *
 * @author ChenMing
 * @date 2021/10/15
 * @see Msg
 * @see MessageTransmitter
 * @see MqServerPropertiesManager
 * @see TransmitterRegister
 * @see TransmitterFactoryBean
 */
public class MessageTransmitterHandlerMapping implements BeanPostProcessor {

    /**
     * key：{@link Msg#getServerId()} or {@link Msg#getClass()}全限定名
     */
    private final Map<String, MessageTransmitter> messageTransmitterMap = new ConcurrentHashMap<>();

    private static ConfigurableListableBeanFactory beanFactory;

    private final MqServerPropertiesManager manager;

    private final TransmitterRegister register;

    public MessageTransmitterHandlerMapping(ConfigurableListableBeanFactory beanFactory) {
        Assert.that(beanFactory).isNotNull("beanFactory not null");
        if (MessageTransmitterHandlerMapping.beanFactory == null) {
            MessageTransmitterHandlerMapping.beanFactory = beanFactory;
        }
        manager = beanFactory.getBean(MqServerPropertiesManager.class);
        register = beanFactory.getBean(TransmitterRegister.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MessageTransmitter) {
            MessageTransmitter transmitter = (MessageTransmitter) bean;
            List<Class<? extends Msg>> types = transmitter.type();
            for (Class<? extends Msg> type : types) {
                //factoryBean在后面的创建过程中有并发问题，此处逻辑不允许覆盖
                synchronized (this) {
                    if (!messageTransmitterMap.keySet().contains(type.getName())) {
                        messageTransmitterMap.put(type.getName(), transmitter);
                    }
                }
            }
            if (bean instanceof AbstractMessageTransmitter) {
                synchronized (this) {
                    String serverId = ((AbstractMessageTransmitter) bean).getServerProperties().getServerId();
                    if (!messageTransmitterMap.keySet().contains(serverId)) {
                        messageTransmitterMap.put(serverId, transmitter);
                    }
                }
            }
        }
        return bean;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 废弃，保留原特性（仅支持单MessageTransmitter）
     * 请使用{@link #getTransfer(Msg)}
     *
     * @param clz {@link Msg}的类型，可能为其子类也可以就是{@link Msg}
     * @return {@link MessageTransmitter}
     */
    @Deprecated
    public MessageTransmitter getTransfer(Class<? extends Msg> clz) {
        Assert.that(clz).isNotNull("传入Class类型为空");
        return messageTransmitterMap.get(clz.getName());
    }

    /**
     * 根据{@link Msg}获取{@link MessageTransmitter}
     * <p> 会根据{@link Msg#getServerId()}获取或者{@link Msg#getClass()}全限定名来获取
     *
     * @param msg 待发送的消息
     * @return {@link MessageTransmitter} 消息发送器
     */
    public MessageTransmitter getTransfer(Msg msg) {
        String serverId = msg.getServerId();
        //为null则可能使用Msg的子类进行映射
        if (StringUtils.isEmpty(serverId)) {
            Assert.that(msg.getClass() == Msg.class).isFalse("使用类型" + Msg.class + "请赋值serverId");
            MessageTransmitter transmitter = messageTransmitterMap.get(msg.getClass().getName());
            if (transmitter == null) {
                throw new MessagingException("未配置的消息类型：" + msg.getClass().getName());
            }
            return transmitter;
        }
        MessageTransmitter transmitter = messageTransmitterMap.get(serverId);
        return transmitter == null ? initializeTransmitter(serverId) : transmitter;
    }

    private MessageTransmitter initializeTransmitter(String serverId) {
        MessageTransmitter transmitter;
        AbstractMqServerProperties properties = manager.getServerProperties(serverId);
        Assert.that(properties).isNotNull("未配置的serverId：" + serverId);
        synchronized (properties) {
            transmitter = messageTransmitterMap.get(serverId);
            if (transmitter == null) {
                transmitter = register.register(properties);
            }
        }
        messageTransmitterMap.put(serverId, transmitter);
        return transmitter;
    }
}
