package com.foco.mq.extend;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foco.context.asserts.Assert;
import com.foco.mq.model.BaseConsumerProperty;
import com.foco.mq.model.BaseProducerProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * 所有扩展的消息中间件配置类都需要集成此抽象类，并且设计相关属性的体系
 * 可以支持yml的方式0代码侵入来实现Mq的发送，做到高度抽象的Api调用方式
 * 双端设计上需要继承（生产端{@link BaseProducerProperty} 消费端{@link BaseConsumerProperty}）
 *
 * @param <S> - {@link AbstractMqServerProperties}的子类，用作区别不同实例(也许在某个消息中间件实例这个名词概念也存在,可以理解为不同集群服务)
 * @param <P> - {@link BaseProducerProperty}producer
 * @param <C> - {@link BaseConsumerProperty}consumer
 * @author ChenMing
 * @version 1.0.0
 * @date 2021/12/29 11:46
 * @see com.foco.mq.core.producer.MessageTransmitterHandlerMapping
 * @since foco2.3.0
 */
@Data
public abstract class AbstractMqServerProperties<S extends AbstractMqServerProperties
        , P extends BaseProducerProperty
        , C extends BaseConsumerProperty> {

    /**
     * 服务Id，在这里为每个服务唯一标识，默认值为继承自{@link AbstractMqServerProperties}的全限定名
     * 无论如何该值都不能为空或空字符串
     */
    protected String serverId;

    /**
     * 多服务配置
     * <p>类型为本类的子类，由继承自本类的子类组成
     * 该值为空或者空集合的时候，表示用户没有使用多实例
     */
    protected List<S> multiServer;

    /**
     * 生产者配置项
     * <p>配置项的主要目的是为了属性的复用，配合{@link BaseProducerProperty#getPropertyId()}
     * 来进行关联映射，减少重复配置项
     * <p>一般情况下生产者的吞吐能力是远远大于消费者，所以很多情况下用户并不会配置该项
     */
    protected List<P> multiProducer;

    /**
     * 实际创建的生产者
     * <p>可以仅配置{@link BaseProducerProperty#getPropertyId()}映射关联
     * {@link #multiProducer}中的propertyId配置项即可
     * <p> 如果关联了{@link BaseProducerProperty#getPropertyId()}的同时也
     * 配置了该属性内的配置内容，则会以该属性内的配置内容为主（覆盖）
     * <p>关于{@link #multiProducer}的Class对象是同一个，对类的职责不清晰
     * 问题，由于Java没有多继承子类在扩展时配置项无法做到很好的扩展，
     * 导致维护配置项的难度提高（需要维护两份）,故采用同一个Class对象
     * TODO 哪怕什么都没配置默认也会产生一个生产者（注：消息中间件扩展者需要实现该机制）
     */
    protected List<P> producers;

    /**
     * 消费者配置项
     * <p>存在意义与{@link #multiProducer}一样，映射类为
     * {@link BaseConsumerProperty#getPropertyId()}
     */
    protected List<C> multiConsumer;

    /**
     * 实际创建的消费者
     * <p>可以仅配置{@link BaseConsumerProperty#getPropertyId()}映射关联
     * {@link #multiConsumer}中的propertyId配置项即可
     * <p> 如果关联了{@link BaseConsumerProperty#getPropertyId()}的同时也
     * 配置了该属性内的配置内容，则会以该属性内的配置内容为主（覆盖）
     * <p>关于{@link #multiConsumer}的Class对象是同一个，对类的职责不
     * 清晰问题，由于Java没有多继承子类在扩展时配置项无法做到很好的扩展，
     * 导致维护配置项的难度提高（需要维护两份）,故采用同一个Class对象
     */
    protected List<C> consumers;

    /**
     * 防止重复相同ServerId
     *
     * @see AbstractMqServerProperties#getServerId()
     */
    @JsonIgnore
    private final static Set<String> SERVER_IDS = new ConcurrentHashSet<>();

    /**
     * 防止生产者重复相同producerId
     *
     * @see BaseProducerProperty#getProducerId()
     */
    @JsonIgnore
    private final static Set<String> PRODUCER_IDS = new ConcurrentHashSet<>();

    /**
     * 防止消费者重复相同consumerId
     *
     * @see BaseConsumerProperty#getConsumerId()
     */
    @JsonIgnore
    private final static Set<String> CONSUMER_IDS = new ConcurrentHashSet<>();

    /**
     * 防止生产者者重复相同propertyId
     *
     * @see BaseConsumerProperty#getConsumerId()
     */
    @JsonIgnore
    private final static Set<String> PRODUCER_PROPERTY_IDS = new ConcurrentHashSet<>();

    /**
     * 防止消费者重复相同propertyId
     *
     * @see BaseConsumerProperty#getConsumerId()
     */
    @JsonIgnore
    private final static Set<String> CONSUMER_PROPERTY_IDS = new ConcurrentHashSet<>();

    public void initializeProperties() {
        serverId = this.getClass().getName();
        initializeServer((S) this);
        initializeMultiServer();
        initializeMultiConsumer();
        initializeConsumers();
        initializeMultiProducer();
        initializeProducers();
        sonInitialize();
    }

    /**
     * 子类实现并对自己所扩展的属性项做初始化
     * <p>可能是校验，也可能重新赋值，取决于子类实现
     */
    protected abstract void sonInitialize();

    /**
     * 子类实现，验证子类的配置项
     *
     * @param properties 子类扩展的配置类校验
     */
    protected abstract void initializeServer(S properties);

    /**
     * 子类实现，自定义验证{@link BaseConsumerProperty}扩展部分
     *
     * @param property 子类扩展的配置属性{@link BaseConsumerProperty}校验
     */
    protected abstract void initializeConsumer(C property);

    /**
     * 子类实现，自定义验证{@link BaseProducerProperty}扩展部分
     *
     * @param property 子类扩展的配置属性{@link BaseProducerProperty}校验
     */
    protected abstract void initializeProducer(P property);

    protected void initializeMultiServer() {
        if (!CollectionUtils.isEmpty(multiServer)) {
            for (S properties : multiServer) {
                Assert.that(properties.getServerId()).isNotEmpty(properties.getClass().getName() + " multi-server配置项有列未配置serverId");
                Assert.that(SERVER_IDS.contains(properties.getServerId())).isFalse(" multi-server配置项存在重复的serverId");
                SERVER_IDS.add(properties.getServerId());
                initializeServer(properties);
            }
        }
    }

    protected void initializeMultiConsumer() {
        if (!CollectionUtils.isEmpty(multiConsumer)) {
            for (BaseConsumerProperty consumerProperty : multiConsumer) {
                Assert.that(consumerProperty.getPropertyId()).isNotEmpty(consumerProperty.getClass().getName() + " multi-consumer配置项有列未配置propertyId");
                Assert.that(CONSUMER_PROPERTY_IDS.contains(consumerProperty.getPropertyId())).isFalse(consumerProperty.getClass().getName() + " multi-consumer配置项存在重复的propertyId");
                CONSUMER_PROPERTY_IDS.add(consumerProperty.getPropertyId());
                if (StringUtils.isEmpty(consumerProperty.getServerId())) {
                    consumerProperty.setServerId(this.getClass().getName());
                }
            }
        }
    }

    protected void initializeMultiProducer() {
        if (!CollectionUtils.isEmpty(multiProducer)) {
            for (BaseProducerProperty producerProperty : multiProducer) {
                Assert.that(producerProperty.getPropertyId()).isNotEmpty(producerProperty.getClass().getName() + " multi-producer配置项有列未配置propertyId");
                Assert.that(PRODUCER_PROPERTY_IDS.contains(producerProperty.getPropertyId())).isFalse(producerProperty.getClass().getName() + " multi-producer配置项存在重复的propertyId");
                PRODUCER_PROPERTY_IDS.add(producerProperty.getPropertyId());
                if (StringUtils.isEmpty(producerProperty.getServerId())) {
                    producerProperty.setServerId(this.getClass().getName());
                }
            }
        }
    }

    protected void initializeConsumers() {
        if (!CollectionUtils.isEmpty(consumers)) {
            for (BaseConsumerProperty consumerProperty : consumers) {
                Assert.that(consumerProperty.getConsumerId()).isNotEmpty(consumerProperty.getClass().getName() + " consumers配置项有列未配置consumerId");
                Assert.that(CONSUMER_IDS.contains(consumerProperty.getConsumerId())).isFalse(consumerProperty.getClass().getName() + " consumers配置项存在重复的consumerId（如果配置了其他消息中间件，请检查其他消息中间件的consumerId配置项）");
                CONSUMER_IDS.add(consumerProperty.getConsumerId());
                if (!StringUtils.isEmpty(consumerProperty.getPropertyId()) && !CollectionUtils.isEmpty(getMultiConsumer())) {
                    for (BaseConsumerProperty c : getMultiConsumer()) {
                        if (c.getPropertyId().equals(consumerProperty.getPropertyId()) && StringUtils.isEmpty(c.getServerId())) {
                            consumerProperty.setServerId(c.getServerId());
                        }
                    }
                }
                if (StringUtils.isEmpty(consumerProperty.getServerId())) {
                    consumerProperty.setServerId(this.getClass().getName());
                }
                initializeConsumer((C) consumerProperty);
            }
        }
    }

    protected void initializeProducers() {
        if (!CollectionUtils.isEmpty(producers)) {
            for (BaseProducerProperty producerProperty : producers) {
                Assert.that(producerProperty.getProducerId()).isNotEmpty(producerProperty.getClass().getName() + " producers配置项有列未配置producerId");
                Assert.that(PRODUCER_IDS.contains(producerProperty.getProducerId())).isFalse(producerProperty.getClass().getName() + " producers配置项存在重复的producerId（如果配置了其他消息中间件，请检查其他消息中间件的producerId配置项）");
                PRODUCER_IDS.add(producerProperty.getProducerId());
                if (!StringUtils.isEmpty(producerProperty.getPropertyId()) && !CollectionUtils.isEmpty(getMultiProducer())) {
                    for (BaseProducerProperty p : getMultiProducer()) {
                        if (p.getPropertyId().equals(producerProperty.getPropertyId()) && StringUtils.isEmpty(p.getServerId())) {
                            producerProperty.setServerId(p.getServerId());
                        }
                    }
                }
                if (StringUtils.isEmpty(producerProperty.getServerId())) {
                    producerProperty.setServerId(this.getClass().getName());
                }
                initializeProducer((P) producerProperty);
            }
        }
    }

    protected P getProducerOptions(String propertyId) {
        if (!CollectionUtils.isEmpty(getMultiProducer())) {
            for (P p : getMultiProducer()) {
                if (p.getPropertyId().equals(propertyId)) {
                    return p;
                }
            }
        }
        return null;
    }

    protected C getConsumerOptions(String propertyId) {
        if (!CollectionUtils.isEmpty(getMultiConsumer())) {
            for (C c : getMultiConsumer()) {
                if (c.getPropertyId().equals(propertyId)) {
                    return c;
                }
            }
        }
        return null;
    }
}
