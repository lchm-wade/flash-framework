package com.foco.mq.idempotent.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.model.constant.FocoConstants;
import com.foco.mq.core.consumer.AbstractConsumerFunction;
import com.foco.mq.exception.MessagingException;
import com.foco.mq.mapper.SendRecordMapper;
import com.foco.mq.model.Msg;
import com.foco.mq.model.SendRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ChenMing
 * @date 2021/11/3
 */
@Slf4j
public class IdempotentConsumerWrapper {
    @Resource
    private SendRecordMapper sendRecordMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    public AbstractConsumerFunction wrapT(AbstractConsumerFunction target, String idCard) {
        return new AbstractConsumerFunction() {
            @Override
            public void targetConsume(Msg message) throws RuntimeException {
                transactionTemplate.execute(transactionStatus -> {
                    try {
                        wrap(target, idCard).targetConsume(message);
                    } catch (Exception e) {
                        transactionStatus.setRollbackOnly();
                        throw new MessagingException(message, e);
                    }
                    return null;
                });
            }
        };
    }

    public AbstractConsumerFunction wrap(AbstractConsumerFunction target, String idCard) {
        return new AbstractConsumerFunction() {
            @Override
            public void targetConsume(Msg msg) throws RuntimeException, InvocationTargetException, IllegalAccessException {
                SendRecord record = pull(msg, idCard);
                if (record == null) {
                    record = new SendRecord();
                    record.setUniqueIdentifier(idCard);
                    record.setKeys(msg.getKeys());
                    record.setMsgBody(msg.getBody());
                    record.setTopic(msg.getTopic());
                    record.setProperties(JSONObject.toJSONString(msg.getProperties()));
                    record.setHeaders(msg.getProperties().get(FocoConstants.HTTP_CONTEXT));
                    record.setDelFlag(1);
                    try {
                        sendRecordMapper.insert(record);
                    } catch (DuplicateKeyException e) {
                        log.warn("DuplicateKeyException重复消费，跳过 topic：{} Keys：{}", msg.getTopic(), msg.getKeys());
                        //重复消费，跳过
                        return;
                    }
                    target.targetConsume(msg);
                } else {
                    log.warn("重复消费，跳过 topic：{} Keys：{}", msg.getTopic(), msg.getKeys());
                }
            }
        };
    }

    /**
     * 获取数据库中的消息记录
     *
     * @param message 当前消费的消息
     * @param idCard  消费唯一性标记
     * @return 数据库数据
     */
    protected SendRecord pull(Msg message, String idCard) {
        return sendRecordMapper.selectOne(Wrappers.lambdaQuery(SendRecord.class)
                .eq(SendRecord::getType, 0)
                .eq(SendRecord::getKeys, message.getKeys())
                .eq(SendRecord::getUniqueIdentifier, idCard));
    }
}
