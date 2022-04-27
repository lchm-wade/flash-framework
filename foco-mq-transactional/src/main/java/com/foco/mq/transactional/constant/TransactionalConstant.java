package com.foco.mq.transactional.constant;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
public interface TransactionalConstant {

    /**
     * 事务消息失败重试定时任务间隔 单位/s
     */
    long RETRY_INTERVAL = 60;
}
