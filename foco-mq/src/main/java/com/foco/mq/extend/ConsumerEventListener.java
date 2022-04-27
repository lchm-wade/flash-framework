package com.foco.mq.extend;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 将每个消费者进行传递让用户进行额外的属性配置或者额外加持
 * TODO 此时的消费者并未启动
 *
 * @param <T> - consumer的类型
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/11 10:36
 * @since foco2.3.0
 */
@FunctionalInterface
public interface ConsumerEventListener<T> {

    /**
     * {@link #onConsumerEvent(Object, String)}的方法命名，供各处进行使用
     * TODO 如果方法命名修改，请将此字段修改为新的方法名
     */
    String METHOD_NAME = "onConsumerEvent";

    /**
     * 对T对象进行额外属性配置或者额外加持
     *
     * @param event      T 对象实例
     * @param consumerId 消费者id
     * @see #METHOD_NAME
     */
    void onConsumerEvent(T event, String consumerId);

    /**
     * 将消费者实例注入进用户扩展的listener中进行再加强的方法进行复用
     *
     * @param event      消费者T的实例
     * @param consumerId 消费者id
     */
    default void inject(T event, String consumerId) {
        Method[] methods = getClass().getMethods();
        //可能存在桥接方法，存在则忽略桥接
        Class<?> targetParamClz = Object.class;
        for (Method method : methods) {
            if (METHOD_NAME.equals(method.getName())) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 2 && parameters[1].getType() == String.class) {
                    targetParamClz = parameters[0].getType();
                    if (targetParamClz != Object.class) {
                        break;
                    }
                }
            }
        }
        if (targetParamClz.isAssignableFrom(event.getClass())) {
            onConsumerEvent(event, consumerId);
        }
    }
}
