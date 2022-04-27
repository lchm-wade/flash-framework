package com.foco.mq.extend;

import com.foco.mq.model.Msg;

/**
 * @author ChenMing
 * @date 2021/11/6
 */
public interface ProductionBeforeProcessor extends ProductionOrdered{

    /**
     * 消息发送前处理器
     *
     * @param msg 用户封装的Msg
     * @return 要使用的Msg实例，无论是原始的还是包装的Msg;
     */
    Msg postProcessBeforeProduction(Msg msg);
}
