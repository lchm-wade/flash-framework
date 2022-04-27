package com.foco.mq.properties;

import com.foco.model.constant.FocoConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ChenMing
 * @date 2021/11/15
 */
@ConfigurationProperties(MqProperties.MQ_PREFIX)
@Data
public class MqProperties {

    public static final String MQ_PREFIX = FocoConstants.CONFIG_PREFIX + "mq";

    /**
     * 默认开启标签路由
     */
    private boolean labelRoute = true;

    /**
     * 日志
     */
    private boolean log;
}
