package com.foco.mq.core.consumer;

import com.foco.mq.core.MqServerPropertiesManager;
import com.foco.mq.extend.ConsumerResolve;
import com.foco.mq.model.BaseConsumerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenMing
 * @date 2021/10/18
 */
@Slf4j
public class ConsumerOwner {

    private List<Annotation> annotations = new ArrayList<>();

    private List<Consumer> consumers = new ArrayList<>();

    private List<ConsumerResolve> consumerResolves;

    private final MqServerPropertiesManager manager;

    private final Environment environment;

    public ConsumerOwner(List<ConsumerResolve> consumerResolves, MqServerPropertiesManager manager, Environment environment) {
        this.environment = environment;
        this.consumerResolves = consumerResolves;
        this.manager = manager;
    }

    public void register(Object obj, Method method) {
        consumerResolves.forEach(consumerResolve -> {
            Annotation declaredAnnotation = method.getDeclaredAnnotation(consumerResolve.annotation());
            if (declaredAnnotation != null) {
                examineParam(method);
                try {
                    synchronized (this) {
                        consumerResolve.resolveConsumer(obj, method, declaredAnnotation);
                        annotations.add(declaredAnnotation);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException("方法：" + obj.getClass() + "#" + method.getName() + "消费者启动异常：" + e.getMessage());
                }
            }
        });
        Consumer consumer = method.getDeclaredAnnotation(Consumer.class);
        if (consumer != null) {
            examineParam(method);
            Assert.isTrue(!consumers.stream().map(Consumer::value).collect(Collectors.toSet()).contains(consumer.value()),
                    "不允许出现重复的consumerId：" + consumer.value() + "，位置：" + obj.getClass().getName() + "#" + method.getName());
            consumers.add(consumer);
            boolean dispose = false;
            for (ConsumerResolve consumerResolve : consumerResolves) {
                BaseConsumerProperty property = manager.getConsumer(environment.resolveRequiredPlaceholders(consumer.value()));
                property.setConsumer(consumer);
                dispose = consumerResolve.resolveConsumer(obj, method, property);
                if (dispose) {
                    break;
                }
            }
            Assert.isTrue(dispose, "无法处理的consumerId：" + consumer.value() + "，未找到相应的解析类");
        }
    }

    void stop() {
        for (ConsumerResolve consumerResolve : consumerResolves) {
            consumerResolve.stop();
        }
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    private void examineParam(Method method) {
        Parameter[] parameters = method.getParameters();
        Assert.isTrue(parameters != null, "Mq消费者中方法名：" + method.getName() + "参数不能为空");
    }

}
