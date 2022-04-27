package com.foco.mq.extend;

import com.foco.mq.model.Msg;

/**
 * @author ChenMing
 * @date 2021/11/6
 */
public interface ConsumeBeforeProcessorSkip extends ConsumeBeforeProcessor {

    /**
     * 消息消费前处理器
     *
     * @param msg 用户封装的Msg（可以对内容进行加持）
     * @return true:跳过消费 false：不跳过
     */
    boolean postProcessBeforeConsumeSkip(Msg msg);
}
