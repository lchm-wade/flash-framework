package com.foco.mq.extend;

import com.foco.mq.MsgProducer;
import com.foco.mq.model.Msg;

import java.util.List;

/**
 * 使用{@link AbstractMessageTransmitter}进行扩展，否则不会被内核所关注
 * 3.0.0将会删除，全局替换为{@link AbstractMessageTransmitter}
 * @author ChenMing
 * @date 2021/10/14
 * @see AbstractMessageTransmitter
 */
@Deprecated
public interface MessageTransmitter extends MsgProducer {

    /**
     * 消息类型
     *
     * @return class
     */
    List<Class<? extends Msg>> type();

}
