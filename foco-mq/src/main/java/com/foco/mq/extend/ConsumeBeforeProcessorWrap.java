package com.foco.mq.extend;

import com.foco.mq.core.consumer.AbstractConsumerFunction;
import com.foco.mq.model.Msg;

/**
 * @author ChenMing
 * @date 2021/11/6
 */
public interface ConsumeBeforeProcessorWrap extends ConsumeBeforeProcessor {

    /**
     * 消息消费前处理器
     *
     * @param msg      用户封装的Msg（可以对内容进行加持）
     * @param function 目标函数
     * @return 包装过后的函数
     */
    AbstractConsumerFunction postProcessBeforeConsumeWrap(Msg msg, AbstractConsumerFunction function);
}
