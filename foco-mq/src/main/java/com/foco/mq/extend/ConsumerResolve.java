package com.foco.mq.extend;

import com.foco.mq.model.BaseConsumerProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ChenMing
 * @date 2021/10/18
 */
public interface ConsumerResolve<T extends Annotation, C> {

    /**
     * 该接口处理注解的类型
     *
     * @return 注解class
     */
    Class<T> annotation();

    /**
     * 解析为消费者
     *
     * @param obj        bean
     * @param method     方法
     * @param annotation 该接口处理注解的类型{@link #annotation()}
     */
    void resolveConsumer(Object obj, Method method, T annotation);

    /**
     * 解析为消费者，会遍历所有中间件的解析类，如果有一个类已经处理了，
     * 那么就会返回true接下来不会被其他解析类处理（consumerId不允许重复）
     *
     * @param obj      bean
     * @param method   方法
     * @param property 消费者相关信息
     * @return true：已处理 false：未处理
     */
    boolean resolveConsumer(Object obj, Method method, BaseConsumerProperty property);

    /**
     * 子类所解析后的Consumer（统一收集）
     *
     * @return 返回该解析类所解析后持有的Consumer
     */
    List<C> getConsumers();

    /**
     * 指示当前正在运行的组件必须停止。
     */
    void stop();
}
