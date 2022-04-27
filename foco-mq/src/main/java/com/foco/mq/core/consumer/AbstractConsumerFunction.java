package com.foco.mq.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.foco.mq.exception.MessagingException;
import com.foco.mq.extend.*;
import com.foco.mq.model.Msg;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenMing
 * @date 2021/10/19
 */
public abstract class AbstractConsumerFunction {

    private static List<ConsumeBeforeProcessor> beforeProcessors;

    private static List<ConsumeAfterProcessor> postProcessors;

    private static volatile boolean initialized;

    /**
     * 将会在3.x废除
     * @see #getBeanFactory() <- 请使用这个
     */
    @Deprecated
    protected AbstractConsumerFunction(ConfigurableListableBeanFactory beanFactory) {
        this();
    }

    protected AbstractConsumerFunction() {
        if (!initialized) {
            synchronized (AbstractConsumerFunction.class) {
                if (!initialized) {
                    Map<String, ConsumeBeforeProcessor> beans = getBeanFactory().getBeansOfType(ConsumeBeforeProcessor.class);
                    AbstractConsumerFunction.beforeProcessors = beans.values().stream().sorted(Comparator.comparing(ConsumeBeforeProcessor::consumeOrder)).collect(Collectors.toList());
                    Map<String, ConsumeAfterProcessor> postBeans = getBeanFactory().getBeansOfType(ConsumeAfterProcessor.class);
                    AbstractConsumerFunction.postProcessors = postBeans.values().stream().sorted(Comparator.comparing(ConsumeAfterProcessor::consumeOrder)).collect(Collectors.toList());
                    initialized = true;
                }
            }
        }
    }


    /**
     * 当前bean被调用时，{@link ResolveConsumerBeanPostProcessor#getBeanFactory()}已经初始化
     *
     * @return beanFactory
     */
    public ConfigurableListableBeanFactory getBeanFactory() {
        return ResolveConsumerBeanPostProcessor.getBeanFactory();
    }

    /**
     * Msg转化为方法参数
     *
     * @param parameters 方法参数
     * @param messageExt rocketMq消息类型
     * @return 方法参数
     */
    protected Object[] parseParams(Parameter[] parameters, Msg messageExt) {
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> pt = parameters[i].getType();
            if (Msg.class.isAssignableFrom(pt)) {
                if (pt == Msg.class) {
                    objects[i] = messageExt;
                } else {
                    throw new MessagingException("异常的类型：" + pt.getName() + "，请使用：" + Msg.class.getName());
                }
            } else {
                objects[i] = JSONObject.parseObject(messageExt.getBody(), pt);
            }
        }
        return objects;
    }

    /**
     * 定义你的消费逻辑
     *
     * @param message 消息
     * @throws RuntimeException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public abstract void targetConsume(Msg message) throws RuntimeException, InvocationTargetException, IllegalAccessException;

    public void consume(Msg message, Annotation consumer) throws RuntimeException, InvocationTargetException, IllegalAccessException {
        boolean skip = false;
        AbstractConsumerFunction function = this;
        for (ConsumeBeforeProcessor processor : beforeProcessors) {
            if (processor instanceof ConsumeBeforeProcessorSkip) {
                skip = ((ConsumeBeforeProcessorSkip) processor).postProcessBeforeConsumeSkip(message);
                if (skip) {
                    break;
                }
            }
            if (processor instanceof ConsumeBeforeProcessorWrap) {
                function = ((ConsumeBeforeProcessorWrap) processor).postProcessBeforeConsumeWrap(message, function);
            }
            if (processor instanceof ConsumeBeforeProcessorConsumer) {
                message = ((ConsumeBeforeProcessorConsumer) processor).postProcessBeforeConsumer(message, consumer);
            }
        }
        try {
            if (!skip) {
                function.targetConsume(message);
            }
        } finally {
            for (ConsumeAfterProcessor postProcessor : postProcessors) {
                try {
                    message = postProcessor.postProcessAfterConsumer(message, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
