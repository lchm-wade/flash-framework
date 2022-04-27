package com.foco.mq.transactional.properties;

import com.foco.mq.constant.MqConstant;
import com.foco.mq.transactional.constant.TransactionalConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
@ConfigurationProperties(TransactionalProperties.TRANSACTIONAL_PREFIX)
@Data
public class TransactionalProperties {

    public static final String TRANSACTIONAL_PREFIX = MqConstant.CONFIG_PREFIX + "transactional";

    /**
     * 事务消息发送失败重试开关
     */
    private boolean retryEnabled = true;

    /**
     * 事务消息失败重试定时任务间隔 单位/s
     *
     * @see com.foco.mq.transactional.constant.TransactionalConstant#RETRY_INTERVAL
     */
    private long retryInterval;

    @PostConstruct
    public void init() {
        if (retryInterval == 0) {
            retryInterval = TransactionalConstant.RETRY_INTERVAL;
        }
    }
}
