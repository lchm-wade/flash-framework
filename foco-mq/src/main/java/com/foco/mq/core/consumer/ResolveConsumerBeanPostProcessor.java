package com.foco.mq.core.consumer;

import com.foco.mq.extend.ConsumerResolve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.SmartLifecycle;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ChenMing
 * @date 2021/10/18
 */
@Slf4j
public class ResolveConsumerBeanPostProcessor implements BeanPostProcessor, SmartLifecycle {

    private static ConfigurableListableBeanFactory beanFactory;

    private ConsumerOwner consumerOwner;

    private volatile boolean isRunning = false;

    private List<Object> beans = new LinkedList<>();

    public ConsumerOwner getConsumerOwner() {
        return consumerOwner;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    private ResolveConsumerBeanPostProcessor() {
    }

    public ResolveConsumerBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        ResolveConsumerBeanPostProcessor.beanFactory = beanFactory;
    }

    /**
     * 主要提供给所有的消费者解析类{@link ConsumerResolve}以及相关衍生类
     * 此bean出现的时刻一定是早于解析类{@link ConsumerResolve}出现
     *
     * @return bean工厂
     */
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static <T> T getBean(Class<T> clz) {
        return beanFactory.getBean(clz);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (hasConsumerAspectAnnotation(bean.getClass())) {
            beans.add(bean);
        }
        return bean;
    }

    private void resolveMethod(Object obj) {
        Class<?> clz = resolveTargetClass(obj);
        //不关注其他访问级别，过滤无用方法提高启动速度，需要被关注则将方法设为public
        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            consumerOwner.register(obj, method);
        }
    }

    private Class resolveTargetClass(Object obj) {
        if (AopUtils.isAopProxy(obj)) {
            return AopUtils.getTargetClass(obj);
        }
        return obj.getClass();
    }

    private boolean hasConsumerAspectAnnotation(Class<?> clz) {
        if (clz == null) {
            return false;
        }
        ConsumerAspect[] annotations = clz.getAnnotationsByType(ConsumerAspect.class);
        return annotations != null && annotations.length != 0;
    }

    @Override
    public void start() {
        if (this.isRunning()) {
            throw new IllegalStateException("container already running. " + this.toString());
        }
        for (Object bean : beans) {
            if (consumerOwner == null) {
                consumerOwner = beanFactory.getBean(ConsumerOwner.class);
            }
            resolveMethod(bean);
        }
        this.setRunning(true);
        log.info("running container by foco-mq: {}", this.toString());
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            consumerOwner.stop();
            this.setRunning(false);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
