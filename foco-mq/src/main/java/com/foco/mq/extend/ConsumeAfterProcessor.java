package com.foco.mq.extend;

import com.foco.mq.model.Msg;

import java.lang.annotation.Annotation;

/**
 *
 * 不管消费与否都会执行，报错也会执行
 *
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/11/24 16:43
 * @since foco2.1.0
 */
public interface ConsumeAfterProcessor extends ConsumeOrdered{

    /**
     * 消息消费后处理器
     *
     * @param msg        用户封装的Msg（可以对内容进行加持）
     * @param annotation 消费者注解
     * @return 要使用的Msg实例，无论是原始的还是包装的Msg;
     */
    Msg postProcessAfterConsumer(Msg msg, Annotation annotation);
}
