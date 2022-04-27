package com.foco.mq.core.producer;

import com.foco.context.asserts.Assert;
import com.foco.context.util.SnowflakeIdWorker;
import com.foco.model.constant.FocoErrorCode;
import com.foco.mq.MsgProducer;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.MqServerPropertiesManager;
import com.foco.mq.exception.MessagingException;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.BaseProducerProperty;
import com.foco.mq.model.Msg;
import com.foco.mq.model.SendResult;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foco.mq.constant.MqConstant.NOT_EXIST_CLUE;

/**
 * @author ChenMing
 * @date 2021/10/18
 */
public class FocoMsgProducer implements MsgProducer {

    private long defaultTimeout = 3000;

    private final MessageTransmitterHandlerMapping transmitterHandlerMapping;

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    private final List<ProductionBeforeProcessor> beforeProcessors;

    private final MqServerPropertiesManager manager;

    public FocoMsgProducer(ConfigurableListableBeanFactory beanFactory) {
        this.transmitterHandlerMapping = beanFactory.getBean(MessageTransmitterHandlerMapping.class);
        Map<String, ProductionBeforeProcessor> beans = beanFactory.getBeansOfType(ProductionBeforeProcessor.class);
        this.beforeProcessors = beans.values().stream().sorted(Comparator.comparing(ProductionBeforeProcessor::productionOrder)).collect(Collectors.toList());
        this.manager = beanFactory.getBean(MqServerPropertiesManager.class);
    }


    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public Msg pretreatmentParam(Msg msg) {
        String producerId = msg.getProducerId();
        if (!StringUtils.isEmpty(producerId)) {
            //根据producerId找到注入后的配置信息进行增强
            decorateMsg(producerId, msg);
        }
        Assert.that(msg.getTopic()).isNotEmpty(FocoErrorCode.TOPIC_NOT_EXIT);
        if (StringUtils.isEmpty(msg.getKeys())) {
            msg.setKeys(String.valueOf(snowflakeIdWorker.nextId()));
        }
        msg.setKeys((msg.getKeys().contains(msg.getTopic()) ? "" : msg.getTopic()) + msg.getKeys());
        msg.put(MsgPropertyConstant.MESSAGE_CLASS, msg.getClass().getName());
        for (ProductionBeforeProcessor processor : beforeProcessors) {
            msg = processor.postProcessBeforeProduction(msg);
        }
        return msg;
    }

    @Override
    public boolean sendT(Msg msg) {
        return send(msg).isSucceed();
    }

    @Override
    public boolean sendT(Msg msg, long timeout) {
        return send(msg, timeout).isSucceed();
    }

    @Override
    public SendResult send(Msg msg) {
        return send(msg, getDefaultTimeout());
    }

    @Override
    public SendResult send(Msg msg, long timeout) {
        msg = pretreatmentParam(msg);
        return transmitterHandlerMapping.getTransfer(msg).send(msg, timeout);
    }

    @Override
    public SendResult sendOrderly(Msg msg) {
        return sendOrderly(msg, getDefaultTimeout());
    }

    @Override
    public SendResult sendOrderly(Msg msg, long timeout) {
        Assert.that(msg.getHashTarget()).isNotEmpty(FocoErrorCode.HASH_TARGET_NOT_EXIT);
        msg = pretreatmentParam(msg);
        return transmitterHandlerMapping.getTransfer(msg).sendOrderly(msg, timeout);
    }

    @Override
    public SendResult sendBatch(LinkedList<Msg> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            return new SendResult();
        }
        LinkedList<Msg> sends = new LinkedList<>();
        msg.forEach(m -> sends.add(pretreatmentParam(m)));
        return transmitterHandlerMapping.getTransfer(msg.get(0)).sendBatch(sends);
    }


    private void decorateMsg(String producerId, Msg msg) {
        BaseProducerProperty producer = manager.getProducer(producerId);
        try {
            String propertyId = producer.getPropertyId();
            if (!StringUtils.isEmpty(propertyId)) {
                BaseProducerProperty options = manager.getProducerOptions(propertyId);
                try {
                    options.decorate(msg);
                } catch (NullPointerException e) {
                    throw new MessagingException(NOT_EXIST_CLUE.replace("{}", "propertyId" + producerId));
                }
            }
            producer.decorate(msg);
        } catch (NullPointerException e) {
            throw new MessagingException(NOT_EXIST_CLUE.replace("{}", "producerId：" + producerId));
        }
    }
}
