package com.foco.mq.transactional.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.context.util.DateUtils;
import com.foco.model.constant.FocoConstants;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.producer.MessageTransmitterHandlerMapping;
import com.foco.mq.mapper.SendRecordMapper;
import com.foco.mq.model.Msg;
import com.foco.mq.model.SendRecord;
import com.foco.mq.model.SendResult;
import com.foco.mq.transactional.properties.TransactionalProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
@Slf4j
public class RetryMessageScheduler {

    @Resource
    private SendRecordMapper recordMapper;

    @Resource
    private TransactionalProperties transactionalProperties;

    @Resource
    @Lazy
    private MessageTransmitterHandlerMapping handlerMapping;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r, "FOCO-MQ-RetryMessage");
        thread.setDaemon(true);
        return thread;
    });

    @PostConstruct
    public void start() {
        if (transactionalProperties.isRetryEnabled()) {
            scheduledExecutorService.schedule(new Scheduler(), 1, TimeUnit.SECONDS);
        }
    }

    public class Scheduler implements Runnable {

        @Override
        public void run() {
            try {
                Set<Object> keys = getFailureMessage();
                for (Object key : keys) {
                    SendRecord sendRecord = recordMapper.selectOne(Wrappers.lambdaQuery(SendRecord.class)
                            .eq(SendRecord::getKeys, key)
                            .eq(SendRecord::getType, 1));
                    String properties = sendRecord.getProperties();
                    if (!StringUtils.isBlank(properties)) {
                        Map<String, String> map = JSONObject.parseObject(properties, Map.class);
                        try {
                            Class.forName(map.get(MsgPropertyConstant.MESSAGE_CLASS));
                        } catch (ClassNotFoundException | NullPointerException e) {
                            log.error("消息类型找不到，发送重试失败，消息体：{}，异常信息：{}", JSONObject.toJSONString(sendRecord), e);
                            sendRecord.setCurrentCount(sendRecord.getMaxCount());
                            sendRecord.setErrorMsg(e.toString());
                            recordMapper.update(sendRecord, Wrappers.lambdaQuery(SendRecord.class)
                                    .eq(SendRecord::getKeys, key)
                                    .eq(SendRecord::getType, 1));
                            continue;
                        }
                        Msg msg = new Msg.Builder().setTopic(sendRecord.getTopic()).setBody(sendRecord.getMsgBody()).build();
                        map.keySet().stream()
                                .filter(k -> k.contains(FocoConstants.CONFIG_PREFIX))
                                .forEach(k -> msg.put(k, map.get(k)));
                        SendResult send = handlerMapping.getTransfer(msg).send(msg);
                        if (!send.isSucceed()) {
                            log.error("事务消息重试异常，Topic：{} keys：{} ,异常信息：{}", msg.getTopic(), msg.getKeys(), JSONObject.toJSONString(send.getResult()));
                            sendRecord.setCurrentCount(sendRecord.getCurrentCount() + 1);
                            recordMapper.update(sendRecord, Wrappers.lambdaQuery(SendRecord.class)
                                    .eq(SendRecord::getKeys, key)
                                    .eq(SendRecord::getType, 1));
                        } else {
                            recordMapper.delete(Wrappers.lambdaQuery(SendRecord.class)
                                    .eq(SendRecord::getKeys, key)
                                    .eq(SendRecord::getType, 1));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scheduledExecutorService.schedule(this, transactionalProperties.getRetryInterval(), TimeUnit.SECONDS);
            }
        }

        private Set<Object> getFailureMessage() {
            //只看一天以内，一般来说需要重推的可能是网络波动导致事务提交后发消息失败或者突然宕机，保证最终一致即可
            String start = DateUtils.dateToStringD(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
            String end = DateUtils.getNowDateDetail();
            //未发送成功的（发送成功会直接物理删除） 注：只查key的原因是为了避免一次性创造大对象
            List<Object> objects = recordMapper.selectObjs(Wrappers.lambdaQuery(SendRecord.class)
                    .select(SendRecord::getKeys)
                    .eq(SendRecord::getType, 1)
                    .eq(SendRecord::getDelFlag, 0)
                    //只关注发送端重试，消费端有mq自己的重试机制
                    .between(SendRecord::getCreateTime, start, end)
                    .last(" and current_count < max_count")
            );
            return new HashSet<>(objects);
        }
    }


}
