package com.foco.mq.model;

import com.foco.mq.core.consumer.Consumer;
import com.foco.mq.extend.ConsumerResolve;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/30 10:30
 * @since foco2.3.0
 */
@Data
public abstract class BaseConsumerProperty {

    /**
     * 消费者Id
     */
    private String consumerId;

    /**
     * 服务id
     */
    private String serverId;

    /**
     * 配置项id
     */
    private String propertyId;

    /**
     * 对应的{@link Consumer}注解
     * <p>{@link com.foco.mq.core.consumer.ConsumerOwner#register(Object, Method)}注册
     * 的时候会被注入，如果你实现了{@link ConsumerResolve}或者是注册之后读取那么必然有值。
     * <p>
     * 注：被直接new的形式创建的消费者不具备{@link Consumer}
     */
    private Consumer consumer;
}
