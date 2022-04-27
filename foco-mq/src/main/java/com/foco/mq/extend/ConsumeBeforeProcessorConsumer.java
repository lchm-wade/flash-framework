package com.foco.mq.extend;

import com.foco.mq.model.Msg;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;

/**
 * {@link Annotation} 可以传入为null，处理时需要注意
 *
 * @author ChenMing
 * @date 2021/11/7
 */
public interface ConsumeBeforeProcessorConsumer extends ConsumeBeforeProcessor {


    /**
     * 消息消费前处理器
     *
     * @param msg        用户封装的Msg（可以对内容进行加持）
     * @param annotation 消费者注解（可能为子类实现的封装注解，也可能是{@link com.foco.mq.core.consumer.Consumer}）
     * @return 要使用的Msg实例，无论是原始的还是包装的Msg;
     */
    Msg postProcessBeforeConsumer(Msg msg, @Nullable Annotation annotation);

}
