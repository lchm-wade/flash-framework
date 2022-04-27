package com.foco.mq.constant;

import com.foco.model.constant.FocoConstants;

/**
 * @author ChenMing
 * @date 2021/10/15
 */
public interface MsgPropertyConstant {
    /*
     * ********************************************************************************
     *                                      公共部分(生产消费都会引用)
     *  ********************************************************************************
     */

    String ROUTE = FocoConstants.CONFIG_PREFIX + "ROUTE";

    String CREATE_TIME = FocoConstants.CONFIG_PREFIX + "CREATE_TIME";

    String DELAY_TIME = FocoConstants.CONFIG_PREFIX + "DELAY_TIME";

    String INITIAL_DELAY_TIME = FocoConstants.CONFIG_PREFIX + "_INITIAL_DELAY_TIME";

    String KEYS = FocoConstants.CONFIG_PREFIX + "KEYS";

    String MSG_BODY_CLZ = FocoConstants.CONFIG_PREFIX + "MSG_BODY_CLZ";

    /**
     * 消息类型
     *
     * @see com.foco.mq.model.Msg 或其子类
     */
    String MESSAGE_CLASS = FocoConstants.CONFIG_PREFIX + "MESSAGE_CLASS";

    /**
     * 标记作用
     * <p>
     * true：事务消息
     */
    String TRANSACTIONAL = FocoConstants.CONFIG_PREFIX + "Transactional";

    /**
     * 消息发送的超时时间
     */
    String TIMEOUT = FocoConstants.CONFIG_PREFIX + "TIMEOUT";

    /*
     * ********************************************************************************
     *                                      生产端
     *  ********************************************************************************
     */

    /**
     * 顺序消息使用，用来判断是否发往同一目标 如rocket的queue或者其他消息队列的
     */
    String HASH_TARGET = FocoConstants.CONFIG_PREFIX + "HASH_TARGET";

    /**
     * 指定生产者时使用，用来判断发往哪一个生产者
     */
    String PRODUCER_ID = FocoConstants.CONFIG_PREFIX + "PRODUCER_ID";

    /**
     * 指定Server时使用，用来判断发往哪一个Server
     */
    String SERVER_ID = FocoConstants.CONFIG_PREFIX + "SERVER_ID";

    /*
     * ********************************************************************************
     *                                      消费端
     *  ********************************************************************************
     */

    /**
     * 幂等使用的唯一性send_record表中字段unique_identifier key
     */
    String ID_CARD = FocoConstants.CONFIG_PREFIX + "IDEMPOTENT_ID_CARD";

    /**
     * 是否开启幂等
     */
    String IDEMPOTENT = FocoConstants.CONFIG_PREFIX + "IDEMPOTENT";

    /**
     * 包裹事务
     */
    String PACKAGE_TRANSACTION = FocoConstants.CONFIG_PREFIX + "PACKAGE_TRANSACTION";
}
