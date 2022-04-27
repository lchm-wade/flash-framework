package com.foco.mq.extend;

import org.springframework.util.Assert;

/**
 * 如果你继承了{@link AbstractMessageTransmitter}并扩展了你自己的Transmitter，交由Spring管理
 * 请注意：也许你只创建了一个实例，并交给Spring管理，但是{@link com.foco.mq.core.producer.TransmitterFactoryBean}
 * 会根据用户所配置的serverId进行更多实例的创建（如果没有使用则不会额外创建），所以在你依赖注入的时候需要注意
 * 增加{@link org.springframework.context.annotation.Primary}可以保证你单实例注入的时候不会找到多个实例而报错
 *
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/12 11:30
 * @see com.foco.mq.extend.MessageTransmitter
 * @see com.foco.mq.core.producer.TransmitterFactoryBean
 * @since foco2.3.0
 */
public abstract class AbstractMessageTransmitter<S extends AbstractMqServerProperties> implements MessageTransmitter {

    protected AbstractMqServerProperties serverProperties;

    public AbstractMessageTransmitter(S serverProperties) {
        Assert.isTrue(serverProperties != null, "空的配置类");
        this.serverProperties = serverProperties;
    }

    public AbstractMqServerProperties getServerProperties() {
        return serverProperties;
    }
}
