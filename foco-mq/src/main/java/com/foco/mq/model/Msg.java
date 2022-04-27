package com.foco.mq.model;

import com.alibaba.fastjson.JSONObject;
import com.foco.model.constant.FocoConstants;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.producer.MessageTransmitterHandlerMapping;
import com.foco.mq.extend.Converter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
public class Msg {

    private String topic;

    private byte[] body;

    private Map<String, String> properties = new ConcurrentHashMap<>();

    private volatile static Map<String, Converter> converterMap;

    public static Msg create(String producerId, Object body) {
        Msg msg = create(producerId, JSONObject.toJSONBytes(body));
        msg.put(MsgPropertyConstant.MSG_BODY_CLZ, body.getClass().getName());
        return msg;
    }

    public static Msg create(String producerId, byte[] body) {
        Msg msg = new Msg();
        msg.put(MsgPropertyConstant.PRODUCER_ID, producerId);
        msg.setBody(body);
        return msg;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String get(String key) {
        return properties.get(key);
    }

    /**
     * 没有“foco.”前缀的key当事务消息失败后重试不会进行重新组装
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        this.properties.put(key, value);
    }

    /**
     * 自动加上“foco.”前缀（如果有则不会加）
     * 这样的属性，当事务消息失败后重试会进行重新组装
     *
     * @param key
     * @param value
     */
    public void putFoco(String key, String value) {
        if (key.contains(FocoConstants.CONFIG_PREFIX)) {
            this.properties.put(key, value);
        } else {
            this.properties.put(FocoConstants.CONFIG_PREFIX + key, value);
        }
    }

    public String getFoco(String key) {
        if (key.contains(FocoConstants.CONFIG_PREFIX)) {
            return this.properties.get(key);
        } else {
            return this.properties.get(FocoConstants.CONFIG_PREFIX + key);
        }
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setRoute(String route) {
        put(MsgPropertyConstant.ROUTE, route);
    }

    public void setKeys(String keys) {
        this.properties.put(MsgPropertyConstant.KEYS, keys);
    }

    public void setServerId(String serverId) {
        this.properties.put(MsgPropertyConstant.SERVER_ID, serverId);
    }

    public void setDelayTime(long second) {
        this.properties.put(MsgPropertyConstant.DELAY_TIME, String.valueOf(second));
        if (StringUtils.isEmpty(properties.get(MsgPropertyConstant.CREATE_TIME))) {
            this.properties.put(MsgPropertyConstant.CREATE_TIME, String.valueOf(System.currentTimeMillis()));
        }
        if (StringUtils.isEmpty(properties.get(MsgPropertyConstant.INITIAL_DELAY_TIME))) {
            this.properties.put(MsgPropertyConstant.INITIAL_DELAY_TIME, String.valueOf(second));
        }
    }

    public void setHashTarget(String hash) {
        this.properties.put(MsgPropertyConstant.HASH_TARGET, hash);
    }

    public String getTopic() {
        return topic;
    }

    public byte[] getBody() {
        return body;
    }

    public String getKeys() {
        return getProperties().get(MsgPropertyConstant.KEYS);
    }

    public String getProducerId() {
        return getProperties().get(MsgPropertyConstant.PRODUCER_ID);
    }

    public String getServerId() {
        return getProperties().get(MsgPropertyConstant.SERVER_ID);
    }

    public String getHashTarget() {
        return getProperties().get(MsgPropertyConstant.HASH_TARGET);
    }

    public String getRoute() {
        return getProperties().get(MsgPropertyConstant.ROUTE);
    }

    public long getDelayTime() {
        String delayTime = getProperties().get(MsgPropertyConstant.DELAY_TIME);
        if (StringUtils.isEmpty(delayTime)) {
            return 0;
        }
        return Long.parseLong(delayTime);
    }

    protected Msg() {
    }

    public static class Builder {

        protected String topic;

        protected byte[] body;

        protected Map<String, String> properties = new HashMap<>();

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Msg.Builder setRoute(String route) {
            putProperties(MsgPropertyConstant.ROUTE, route);
            return this;
        }

        public Builder setDelayTime(long second) {
            this.properties.put(MsgPropertyConstant.DELAY_TIME, String.valueOf(second));
            if (StringUtils.isEmpty(properties.get(MsgPropertyConstant.CREATE_TIME))) {
                this.properties.put(MsgPropertyConstant.CREATE_TIME, String.valueOf(System.currentTimeMillis()));
            }
            if (StringUtils.isEmpty(properties.get(MsgPropertyConstant.INITIAL_DELAY_TIME))) {
                this.properties.put(MsgPropertyConstant.INITIAL_DELAY_TIME, String.valueOf(second));
            }
            return this;
        }

        public Builder setProperties(Map<String, String> properties) {
            this.properties = properties;
            return this;
        }

        public Builder setKeys(String keys) {
            this.properties.put(MsgPropertyConstant.KEYS, keys);
            return this;
        }

        public Builder setServerId(String serverId) {
            this.properties.put(MsgPropertyConstant.SERVER_ID, serverId);
            return this;
        }

        public Builder setHashTarget(String hash) {
            this.properties.put(MsgPropertyConstant.HASH_TARGET, hash);
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder setBody(Object body) {
            this.properties.put(MsgPropertyConstant.MSG_BODY_CLZ, body.getClass().getName());
            this.body = JSONObject.toJSONBytes(body);
            return this;
        }

        /**
         * 没有“foco.”前缀的key当事务消息发送失败后重试不会进行重新组装
         *
         * @param key
         * @param value
         * @see Msg#putFoco(String, String) 可以使用此方法
         */
        public Builder putProperties(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        public Msg build() {
            Msg msg = new Msg();
            msg.setBody(body);
            msg.setTopic(topic);
            msg.setProperties(properties);
            return msg;
        }
    }

    public <T> T covert(Class<T> clz) {
        if (converterMap == null) {
            synchronized (Msg.class) {
                if (converterMap == null) {
                    initConvert();
                }
            }
        }
        Converter converter = converterMap.get(clz.getName());
        return converter == null ? null : (T) converter.convert(this);
    }

    private void initConvert() {
        ConfigurableListableBeanFactory beanFactory = MessageTransmitterHandlerMapping.getBeanFactory();
        Map<String, Converter> beans = beanFactory.getBeansOfType(Converter.class);
        converterMap = new ConcurrentHashMap<>(8);
        for (Converter converter : beans.values()) {
            converterMap.put(converter.type().getName(), converter);
        }
    }
}
