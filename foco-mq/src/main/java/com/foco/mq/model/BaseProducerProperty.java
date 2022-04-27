package com.foco.mq.model;

import com.foco.mq.extend.AbstractMqServerProperties;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/30 10:31
 * @since foco2.3.0
 */
@Data
public abstract class BaseProducerProperty {

    /**
     * 生产者Id
     */
    private String producerId;

    /**
     * 服务id
     */
    private String serverId;

    /**
     * 配置项id
     */
    private String propertyId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 全局唯一性id （一般用作幂等，不填会自动生成）
     */
    private String keys;

    /**
     * 延时时间 单位/s
     */
    private Long delayTime;

    /**
     * hash值，值一致则会去往相同队列（不同消息中间件称呼不一样，可以理解为顺序消息时使用）
     */
    private String hashTarget;


    public void decorate(Msg msg) {
        if (!StringUtils.isEmpty(topic)) {
            msg.setTopic(topic);
        }
        if (!StringUtils.isEmpty(keys)) {
            msg.setKeys(keys);
        }
        if (!StringUtils.isEmpty(hashTarget)) {
            msg.setHashTarget(hashTarget);
        }
        if (delayTime != null && delayTime != 0) {
            msg.setDelayTime(delayTime);
        }
        if (StringUtils.isEmpty(serverId)) {
            msg.setServerId(sonServerProperties().getName());
        } else {
            msg.setServerId(serverId);
        }
        sonDecorate(msg);
    }

    /**
     * 子类实现，根据{@link BaseProducerProperty}子类的属性对
     * {@link Msg}的内容进行装饰
     *
     * @param msg 待装饰的消息
     */
    protected abstract void sonDecorate(Msg msg);

    /**
     * 子类返回客户端的配置类类型（注：各中间件默认serverId为配置类全限定名）
     *
     * @return 子类配置类类型
     * @see AbstractMqServerProperties
     */
    protected abstract Class<? extends AbstractMqServerProperties> sonServerProperties();
}
