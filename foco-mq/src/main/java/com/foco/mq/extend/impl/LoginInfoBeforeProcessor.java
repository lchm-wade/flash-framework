package com.foco.mq.extend.impl;

import com.alibaba.fastjson.JSON;
import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextConstant;
import com.foco.context.core.LoginContextHolder;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.extend.ConsumeBeforeProcessorConsumer;
import com.foco.mq.extend.ConsumeAfterProcessor;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.Msg;

import java.lang.annotation.Annotation;

/**
 * @author ChenMing
 * @date 2021/11/6
 */
public class LoginInfoBeforeProcessor implements ProductionBeforeProcessor, ConsumeBeforeProcessorConsumer, ConsumeAfterProcessor {

    @Override
    public Msg postProcessBeforeConsumer(Msg msg, Annotation annotation) {
        Object obj = msg.getFoco(LoginContextConstant.LOGIN_CONTEXT);
        if (obj != null) {
            LoginContextHolder.set(String.valueOf(obj));
        }
        return msg;
    }

    @Override
    public Msg postProcessBeforeProduction(Msg msg) {
        LoginContext loginContext = LoginContextHolder.getLoginContext(LoginContext.class);
        if (loginContext != null) {
            //为了避免消息数据太多，消息队列中只传递主要的上下文数据，如用户id
            LoginContext newLoginContext = new LoginContext();
            newLoginContext.setUserId(loginContext.getUserId());
            newLoginContext.setUserName(loginContext.getUserName());
            newLoginContext.setTenantId(loginContext.getTenantId());
            msg.putFoco(LoginContextConstant.LOGIN_CONTEXT, JSON.toJSONString(newLoginContext));
        }
        return msg;
    }

    @Override
    public int consumeOrder() {
        return MqConstant.LOGIN_INFO_BEFORE_PROCESSOR_WRAP_ORDER;
    }

    @Override
    public Msg postProcessAfterConsumer(Msg msg, Annotation annotation) {
        LoginContextHolder.remove();
        return msg;
    }
}
