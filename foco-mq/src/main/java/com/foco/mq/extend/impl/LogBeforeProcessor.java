package com.foco.mq.extend.impl;

import com.alibaba.fastjson.JSONObject;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.extend.ConsumeBeforeProcessorConsumer;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author ChenMing
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/16 17:03
 * @since foco2.1.0
 */
@Slf4j
public class LogBeforeProcessor implements ProductionBeforeProcessor, ConsumeBeforeProcessorConsumer {

    @Override
    public Msg postProcessBeforeProduction(Msg msg) {
        String body = bodyAssemble(msg);
        log.info("FocoMq  Send---消息日志，topic：{}，properties：{}，body：{}", msg.getTopic()
                , JSONObject.toJSONString(msg.getProperties()), body);
        return msg;
    }

    private String bodyAssemble(Msg msg) {
        String clzName = msg.get(MsgPropertyConstant.MSG_BODY_CLZ);
        String body = null;
        if (!StringUtils.isEmpty(clzName)) {
            try {
                Class<?> clz = Class.forName(clzName);
                if (clz.isAssignableFrom(Number.class)) {
                    body = JSONObject.parseObject(msg.getBody(), clz).toString();
                } else if (clz.isAssignableFrom(String.class)) {
                    body = JSONObject.parseObject(msg.getBody(), clz);
                } else {
                    body = JSONObject.toJSONString(JSONObject.parseObject(msg.getBody(), clz));
                }
            } catch (ClassNotFoundException ignored) {
                //ignored
                log.warn("ClassNotFoundException : {}", clzName);
            } catch (Exception e) {
                log.error("FocoMq日志打印异常！！body参数全限定名：{}",clzName);
            }
        }
        if (StringUtils.isEmpty(body)) {
            body = Arrays.toString(msg.getBody());
        }
        return body;
    }

    @Override
    public int productionOrder() {
        return MqConstant.LOG_BEFORE_PROCESSOR_WRAP_ORDER;
    }

    @Override
    public Msg postProcessBeforeConsumer(Msg msg, Annotation annotation) {
        String body = bodyAssemble(msg);
        log.info("FocoMq  Receive---消息日志，topic：{}，properties：{}，body：{}", msg.getTopic()
                , JSONObject.toJSONString(msg.getProperties()), body);
        return msg;
    }

    @Override
    public int consumeOrder() {
        return MqConstant.LOG_BEFORE_PROCESSOR_WRAP_ORDER;
    }
}
