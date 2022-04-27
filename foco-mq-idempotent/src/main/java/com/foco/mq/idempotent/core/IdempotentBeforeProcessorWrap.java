package com.foco.mq.idempotent.core;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.consumer.AbstractConsumerFunction;
import com.foco.mq.extend.ConsumeBeforeProcessorWrap;
import com.foco.mq.model.Msg;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Map;

/**
 * @author ChenMing
 * @date 2021/11/3
 */
public class IdempotentBeforeProcessorWrap implements ConsumeBeforeProcessorWrap {
    private final IdempotentConsumerWrapper idempotentConsumerWrapper;

    public IdempotentBeforeProcessorWrap(ConfigurableListableBeanFactory beanFactory) {
        this.idempotentConsumerWrapper = beanFactory.getBean(IdempotentConsumerWrapper.class);
    }

    @Override
    public AbstractConsumerFunction postProcessBeforeConsumeWrap(Msg msg, AbstractConsumerFunction function) {
        Map<String, String> properties = msg.getProperties();
        String idempotent = properties.get(MsgPropertyConstant.IDEMPOTENT);
        if (Boolean.parseBoolean(idempotent)) {
            String idCard = properties.get(MsgPropertyConstant.ID_CARD);
            String packageTransaction = properties.get(MsgPropertyConstant.PACKAGE_TRANSACTION);
            boolean parseBoolean = Boolean.parseBoolean(packageTransaction);
            if (StringUtils.isBlank(packageTransaction) || !parseBoolean) {
                return idempotentConsumerWrapper.wrap(function, idCard);
            } else {
                return idempotentConsumerWrapper.wrapT(function, idCard);
            }
        } else {
            return function;
        }
    }

    @Override
    public int consumeOrder() {
        return MqConstant.IDEMPOTENT_BEFORE_PROCESSOR_WRAP_ORDER;
    }
}