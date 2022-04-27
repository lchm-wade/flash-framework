package com.foco.mq;


import com.foco.mq.model.Msg;
import com.foco.mq.model.SendResult;

import java.util.LinkedList;

/**
 * 生产端API
 *
 * @author ChenMing
 * @date 2021/10/14
 */
public interface MsgProducer {
    /**
     * 发送事务消息
     *
     * @param msg 消息
     * @return true：成功 false：失败（会自动重试，告知初次发送时的状态，不作处理可忽略）
     */
    boolean sendT(Msg msg);

    /**
     * 发送事务消息
     *
     * @param msg  消息
     * @param timeout 超时时间 单位/ms
     * @return true：成功 false：失败（会自动重试，告知初次发送时的状态，不作处理可忽略）
     */
    boolean sendT(Msg msg, long timeout);

    /**
     * 发送普通消息
     *
     * @param msg 消息
     * @return SendResult
     */
    SendResult send(Msg msg);

    /**
     * 发送普通消息
     *
     * @param msg  消息
     * @param timeout 超时时间 单位/ms
     * @return SendResult
     */
    SendResult send(Msg msg, long timeout);

    /**
     * 发送顺序消息
     *
     * @param msg 消息 设置setHashTarget（String）来进行queue（rocketMQ）的分发，
     *            如果需要发往同一个queue，那么需要设成一样,可以定义成常量
     * @return SendResult
     */
    SendResult sendOrderly(Msg msg);

    /**
     * 发送顺序消息
     *
     * @param msg 消息 设置setHashTarget（String）来进行queue（rocketMQ）的分发，
     *            如果需要发往同一个queue，那么需要设成一样,可以定义成常量
     * @param timeout 超时时间 单位/ms
     * @return SendResult
     */
    SendResult sendOrderly(Msg msg, long timeout);

    /**
     * 批量发送消息
     * 同时保证最终一致性事务、顺序性
     *
     * @param msg 消息
     * @return SendResult
     */
    SendResult sendBatch(LinkedList<Msg> msg);
}
