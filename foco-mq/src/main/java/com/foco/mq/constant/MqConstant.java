package com.foco.mq.constant;

import com.foco.model.constant.FocoConstants;
import com.foco.mq.extend.AbstractMqServerProperties;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
public interface MqConstant {

    String CONFIG_PREFIX = FocoConstants.CONFIG_PREFIX + "mq.";

    /**
     * 用作{@link AbstractMqServerProperties#getServerId()} 默认赋值为{@code Master+_XXX}
     */
    String DEFAULT_SERVER_ID = "Master";

    String SEPARATOR = "@@";

    int ROUTE_BEFORE_PROCESSOR_WRAP_ORDER = -1000;

    /**
     * 幂等性处理，尽量优先在{@link com.foco.mq.extend.ConsumeBeforeProcessorSkip}等处理器实现后
     * 因为很有可能该操作就要被跳过可以让事务粒度更小
     */
    int IDEMPOTENT_BEFORE_PROCESSOR_WRAP_ORDER = -500;

    int HTTP_CONTEXT_BEFORE_PROCESSOR_WRAP_ORDER = -10;

    /**
     * 已废除，如果你在使用中请删除此项
     * <p> 将于3.x进行删除
     */
    @Deprecated
    int TRACE_INFO_BEFORE_PROCESSOR_WRAP_ORDER = -5;

    int LOGIN_INFO_BEFORE_PROCESSOR_WRAP_ORDER = -1;

    int LOG_BEFORE_PROCESSOR_WRAP_ORDER = Integer.MAX_VALUE;

    String NOT_EMPTY_CLUE = "{}不能为空，如果已配置请确认是否被别处覆盖";

    String NOT_EXIST_CLUE = "不存在的{}，请检查yml或确保是否仅有一处配置（多处会被别处覆盖）";
}
