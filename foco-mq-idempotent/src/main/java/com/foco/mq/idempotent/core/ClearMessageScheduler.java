package com.foco.mq.idempotent.core;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.context.util.DateUtils;
import com.foco.mq.idempotent.properties.IdempotentProperties;
import com.foco.mq.mapper.SendRecordMapper;
import com.foco.mq.model.SendRecord;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
@Slf4j
public class ClearMessageScheduler {

    @Resource
    private SendRecordMapper recordMapper;

    @Resource
    private IdempotentProperties properties;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r, "FOCO-MQ-ClearMessage");
        thread.setDaemon(true);
        return thread;
    });

    @PostConstruct
    public void start() {
        if (properties.isClearEnabled()) {
            scheduledExecutorService.schedule(new Scheduler(), 1, TimeUnit.SECONDS);
        }
    }

    public class Scheduler implements Runnable {

        @Override
        public void run() {
            try {
                String time = DateUtils.dateToStringD(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(properties.getScope())));
                recordMapper.delete(Wrappers.lambdaQuery(SendRecord.class)
                        .eq(SendRecord::getType, 0)
                        .eq(SendRecord::getDelFlag, 1)
                        //只关注发送端重试，消费端有mq自己的重试机制
                        .le(SendRecord::getCreateTime, time));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scheduledExecutorService.schedule(this, properties.getIntervalTime(), TimeUnit.MILLISECONDS);
            }
        }
    }


}
