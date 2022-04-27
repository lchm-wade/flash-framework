package com.foco.mq.idempotent.properties;

import com.foco.mq.constant.MqConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
@ConfigurationProperties(IdempotentProperties.IDEMPOTENT_PREFIX)
@Data
public class IdempotentProperties {

    public static final String IDEMPOTENT_PREFIX = MqConstant.CONFIG_PREFIX + "idempotent";

    /**
     * 是否开启清除幂等产生的消费数据定时任务
     */
    private boolean clearEnabled = true;

    /**
     * 检查间隔时间 单位/ms 默认60分钟
     */
    private long intervalTime = 1000 * 60 * 60;

    /**
     * 清除范围 单位 /min 默认15天
     */
    private long scope = 60 * 24 * 15;
}
