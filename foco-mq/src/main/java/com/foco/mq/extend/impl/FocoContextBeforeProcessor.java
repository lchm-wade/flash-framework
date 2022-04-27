package com.foco.mq.extend.impl;

import com.foco.context.core.FocoContextManager;
import com.foco.mq.extend.ConsumeBeforeProcessorConsumer;
import com.foco.mq.extend.ConsumeAfterProcessor;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.Msg;

import java.lang.annotation.Annotation;

/**
 * @author ChenMing
 * @date 2021/11/22
 */
public class FocoContextBeforeProcessor implements ConsumeBeforeProcessorConsumer, ConsumeAfterProcessor, ProductionBeforeProcessor {

    @Override
    public Msg postProcessBeforeConsumer(Msg msg, Annotation annotation) {
        FocoContextManager.setLocal(msg::getFoco);
        return msg;
    }

    @Override
    public Msg postProcessBeforeProduction(Msg msg) {
        FocoContextManager.setHeader(msg::putFoco);
        return msg;
    }

    @Override
    public Msg postProcessAfterConsumer(Msg msg, Annotation annotation) {
        FocoContextManager.remove();
        return msg;
    }
}
