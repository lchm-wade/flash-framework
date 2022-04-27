package com.foco.mq.core;

import com.foco.context.asserts.Assert;
import com.foco.mq.extend.AbstractMqServerProperties;
import com.foco.mq.model.BaseConsumerProperty;
import com.foco.mq.model.BaseProducerProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/31 16:14
 * @see com.foco.mq.core.producer.MessageTransmitterHandlerMapping
 * @see com.foco.mq.extend.AbstractMqServerProperties
 * @since foco2.3.0
 */
public class MqServerPropertiesManager implements BeanPostProcessor {

    /**
     * 配置存储项
     * <p>消息中间件级别，每个消息中间件都会有一个配置
     * key：{@link AbstractMqServerProperties#getServerId()}
     * <p>由于{@link AbstractMqServerProperties#getMultiServer()}存在多实例配置，
     * 需进行递归展开将所有实例配置平铺
     */
    private final Map<String, AbstractMqServerProperties> serverProperties = new ConcurrentHashMap<>();

    /**
     * 消费者配置存储项
     * key : {@link BaseConsumerProperty#getPropertyId()}
     * <p>展开将所有子类中{@link AbstractMqServerProperties#getMultiConsumer()}配置平铺
     */
    private final Map<String, BaseConsumerProperty> multiConsumerMap = new ConcurrentHashMap<>(32);

    /**
     * 消费者存储项
     * key : {@link BaseConsumerProperty#getConsumerId()}
     * <p>展开将所有子类中{@link AbstractMqServerProperties#getConsumers()} 平铺
     */
    private final Map<String, BaseConsumerProperty> consumerMap = new ConcurrentHashMap<>(128);

    /**
     * 消费者来源的配置项
     * key : {@link BaseConsumerProperty#getConsumerId()}
     * <p>映射了配置该{@link BaseConsumerProperty#getConsumerId()}的{@link AbstractMqServerProperties}
     */
    private final Map<String, AbstractMqServerProperties> consumerPropertiesMapping = new ConcurrentHashMap<>(128);

    /**
     * 生产者配置存储项
     * key : {@link BaseProducerProperty#getPropertyId()}
     * <p>展开将所有子类中{@link AbstractMqServerProperties#getMultiProducer()} 平铺
     */
    private final Map<String, BaseProducerProperty> multiProducerMap = new ConcurrentHashMap<>(8);

    /**
     * 生产者存储项
     * key : {@link BaseProducerProperty#getProducerId()}
     * <p>展开将所有子类中{@link AbstractMqServerProperties#getProducers()} ()} 平铺
     */
    private final Map<String, BaseProducerProperty> producerMap = new ConcurrentHashMap<>(128);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractMqServerProperties) {
            synchronized (this) {
                //可能由于健康检查或者预先请求导致多处同时进入，做预处理
                if (!serverProperties.keySet().contains(bean.getClass().getName())) {
                    ((AbstractMqServerProperties) bean).initializeProperties();
                    registerServerProperties((AbstractMqServerProperties) bean);
                }
            }
        }
        return bean;
    }

    /**
     * 注册所有的配置类，将{@link AbstractMqServerProperties#getMultiServer()}、
     * {@link AbstractMqServerProperties#getMultiProducer()}、
     * {@link AbstractMqServerProperties#getProducers()}、
     * {@link AbstractMqServerProperties#getMultiConsumer()}、
     * {@link AbstractMqServerProperties#getConsumers()}、展开
     * 存放到{@link MqServerPropertiesManager}中，集中管理配置
     *
     * @param abstractMqServerProperties 抽象配置类
     */
    private void registerServerProperties(AbstractMqServerProperties abstractMqServerProperties) {
        Assert.that(abstractMqServerProperties.getServerId()).isNotEmpty("serverId不能为空");
        Assert.that(this.serverProperties.keySet().contains(abstractMqServerProperties.getServerId())).isFalse("重复的ServerId：" + abstractMqServerProperties.getServerId());
        this.serverProperties.put(abstractMqServerProperties.getServerId(), abstractMqServerProperties);
        List multiServers = abstractMqServerProperties.getMultiServer();
        if (!CollectionUtils.isEmpty(multiServers)) {
            for (Object multiServer : multiServers) {
                registerServerProperties((AbstractMqServerProperties) multiServer);
            }
        }
        List multiConsumer = abstractMqServerProperties.getMultiConsumer();
        if (!CollectionUtils.isEmpty(multiConsumer)) {
            for (Object consumer : multiConsumer) {
                BaseConsumerProperty consumerProperty = (BaseConsumerProperty) consumer;
                multiConsumerMap.put(consumerProperty.getPropertyId(), consumerProperty);
            }
        }
        List consumers = abstractMqServerProperties.getConsumers();
        if (!CollectionUtils.isEmpty(consumers)) {
            for (Object consumer : consumers) {
                BaseConsumerProperty consumerProperty = (BaseConsumerProperty) consumer;
                consumerMap.put(consumerProperty.getConsumerId(), consumerProperty);
                if (!StringUtils.isEmpty(consumerProperty.getServerId())) {
                    AbstractMqServerProperties properties = getServerProperties(consumerProperty.getServerId());
                    if (properties != null) {
                        consumerPropertiesMapping.put(consumerProperty.getConsumerId(), properties);
                    } else if (!StringUtils.isEmpty(consumerProperty.getPropertyId())) {
                        BaseConsumerProperty options = getConsumerOptions(consumerProperty.getPropertyId());
                        AbstractMqServerProperties optionsProperties = getServerProperties(options.getServerId());
                        consumerPropertiesMapping.put(consumerProperty.getConsumerId(), optionsProperties);
                    } else {
                        throw new UnsupportedOperationException("未配置");
                    }
                } else {
                    consumerPropertiesMapping.put(consumerProperty.getConsumerId(), abstractMqServerProperties);
                }
            }
        }
        List multiProducer = abstractMqServerProperties.getMultiProducer();
        if (!CollectionUtils.isEmpty(multiProducer)) {
            for (Object producer : multiProducer) {
                BaseProducerProperty producerProperty = (BaseProducerProperty) producer;
                multiProducerMap.put(producerProperty.getPropertyId(), producerProperty);
            }
        }
        List producers = abstractMqServerProperties.getProducers();
        if (!CollectionUtils.isEmpty(producers)) {
            for (Object producer : producers) {
                BaseProducerProperty producerProperty = (BaseProducerProperty) producer;
                producerMap.put(producerProperty.getProducerId(), producerProperty);
            }
        }
    }


    /**
     * 服务配置
     *
     * @param serverId 服务id
     * @return 配置项
     */
    public AbstractMqServerProperties getServerProperties(String serverId) {
        return serverProperties.get(serverId);
    }

    /**
     * 消费者配置项
     *
     * @param propertyId 配置id
     * @return 消费者配置项
     */
    public BaseConsumerProperty getConsumerOptions(String propertyId) {
        return multiConsumerMap.get(propertyId);
    }

    /**
     * 消费者
     *
     * @param consumerId 消费者id
     * @return 消费者 （可能此对象内没有配置项只有ProducerId，是关联{@link AbstractMqServerProperties#getMultiConsumer()}的方式来获取）
     */
    public BaseConsumerProperty getConsumer(String consumerId) {
        return consumerMap.get(consumerId);
    }

    /**
     * 生产者配置项
     *
     * @param propertyId 配置id
     * @return 生产配置项
     */
    public BaseProducerProperty getProducerOptions(String propertyId) {
        return multiProducerMap.get(propertyId);
    }

    /**
     * 生产者
     *
     * @param producerId 生产者id
     * @return 生产者
     */
    public BaseProducerProperty getProducer(String producerId) {
        return producerMap.get(producerId);
    }

    /**
     * 根据{@code value}获取配置该{@code value}的{@link AbstractMqServerProperties}
     *
     * @param consumerId 消费者id
     * @return {@link AbstractMqServerProperties}配置项
     */
    public AbstractMqServerProperties getPropertiesByConsumerId(String consumerId) {
        return consumerPropertiesMapping.get(consumerId);
    }
}
