package com.foco.mq.transactional.core;

import com.alibaba.fastjson.JSONObject;
import com.foco.model.constant.FocoConstants;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.producer.FocoMsgProducer;
import com.foco.mq.mapper.SendRecordMapper;
import com.foco.mq.model.Msg;
import com.foco.mq.model.SendRecord;
import com.foco.mq.transactional.aspect.AfterTransactionAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ChenMing
 * @date 2021/10/18
 */
@Slf4j
public class TransactionalMsgProducer extends FocoMsgProducer {

    private AfterTransactionAspect afterTransactionAspect;

    private final SendRecordMapper recordMapper;

    public TransactionalMsgProducer(ConfigurableListableBeanFactory beanFactory) {
        super(beanFactory);
        this.afterTransactionAspect = beanFactory.getBean(AfterTransactionAspect.class);
        this.recordMapper = beanFactory.getBean(SendRecordMapper.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean sendT(Msg msg) {
        return this.sendT(msg, getDefaultTimeout());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean sendT(Msg msg, long timeout) {
        msg.put(MsgPropertyConstant.TRANSACTIONAL, Boolean.TRUE.toString());
        super.pretreatmentParam(msg);
        try {
            recordMapper.insert(createDbMsg(msg));
        } catch (DuplicateKeyException ignored) {
            log.warn("重复发送事务消息，忽略并返回false，topic：{} keys：{}", msg.getTopic(), msg.getKeys());
            return false;
        }
        msg.put(MsgPropertyConstant.TIMEOUT, String.valueOf(timeout));
        afterTransactionAspect.add(msg);
        return true;
    }

    private SendRecord createDbMsg(Msg msg) {
        SendRecord sendRecord = new SendRecord();
        sendRecord.setType(1);
        sendRecord.setTopic(msg.getTopic());
        sendRecord.setKeys(msg.getKeys());
        sendRecord.setMsgBody(msg.getBody());
        sendRecord.setUniqueIdentifier(MsgPropertyConstant.TRANSACTIONAL);
        sendRecord.setProperties(JSONObject.toJSONString(msg.getProperties()));
        sendRecord.setHeaders(msg.getProperties().get(FocoConstants.HTTP_CONTEXT));
        return sendRecord;
    }
}
