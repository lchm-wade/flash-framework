package com.foco.mq.extend;

import com.foco.mq.model.Msg;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
public interface Converter<T> {


    /**
     * 消息类型
     *
     * @return class
     */
    Class<T> type();

    /**
     * Msg转换为其他客户端消息，例org.apache.rocketmq.common.message.Message
     * @param msg foco消息
     * @return 被转换的消息类型
     */
    T convert(Msg msg);

}
