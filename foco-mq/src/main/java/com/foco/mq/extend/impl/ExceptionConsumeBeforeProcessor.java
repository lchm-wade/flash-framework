package com.foco.mq.extend.impl;

import com.alibaba.fastjson.JSONObject;
import com.foco.mq.core.consumer.AbstractConsumerFunction;
import com.foco.mq.extend.ConsumeBeforeProcessorWrap;
import com.foco.mq.model.Msg;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @author ChenMing
 * @date 2021/11/7
 */
@Slf4j
public class ExceptionConsumeBeforeProcessor implements ConsumeBeforeProcessorWrap {

    @Override
    public AbstractConsumerFunction postProcessBeforeConsumeWrap(Msg msg, AbstractConsumerFunction function) {
        return new AbstractConsumerFunction() {
            @Override
            public void targetConsume(Msg message) throws RuntimeException, InvocationTargetException, IllegalAccessException {
                try {
                    function.targetConsume(message);
                } catch (Exception e) {
                    log.error("\n消费异常，影响消息内容properties：{}", JSONObject.toJSONString(message.getProperties()), e);
                    throw e;
                }
            }
        };
    }

}
