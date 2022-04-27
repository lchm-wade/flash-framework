package com.foco.mq.extend.impl;

import com.alibaba.fastjson.JSONObject;
import com.foco.context.util.HttpContext;
import com.foco.model.constant.FocoConstants;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.extend.ConsumeBeforeProcessorConsumer;
import com.foco.mq.extend.ConsumeAfterProcessor;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.Msg;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author ChenMing
 * @date 2021/11/7
 */
public class HttpContextBeforeProcessor implements ProductionBeforeProcessor, ConsumeBeforeProcessorConsumer, ConsumeAfterProcessor {

    @Override
    public Msg postProcessBeforeConsumer(Msg msg, Annotation annotation) {
        String property = msg.get(FocoConstants.HTTP_CONTEXT);
        if (!StringUtils.isEmpty(property)) {
            HttpContext.setHeaders(JSONObject.parseObject(property, Map.class));
        }
        return msg;
    }

    @Override
    public Msg postProcessBeforeProduction(Msg msg) {
        msg.put(FocoConstants.HTTP_CONTEXT, JSONObject.toJSONString(HttpContext.getHeaders()));
        return msg;
    }

    @Override
    public int consumeOrder() {
        return MqConstant.HTTP_CONTEXT_BEFORE_PROCESSOR_WRAP_ORDER;
    }

    @Override
    public Msg postProcessAfterConsumer(Msg msg, Annotation annotation) {
        HttpContext.cleanHeaders();
        return msg;
    }
}
