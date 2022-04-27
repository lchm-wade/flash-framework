package com.foco.distributed.id.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/2 14:43
 **/
@ConfigurationProperties(prefix = IdGenerateProperties.PREFIX)
@Getter
@Setter
public class IdGenerateProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"id.generate";
    public static IdGenerateProperties getConfig(){
        return getConfig(IdGenerateProperties.class);
    }
    /**
     * 百分比
     * (最大值-当前值)/步长
     * (max-current)/step
     * 当剩余号码/步长 小于 30% 进行扩容
     */
    private Integer percent=30;
    /**
     * 号段续租检测任务执行周期,默认1000,单位毫秒
     */
    private Long timeBetweenEvictionRunsMillis=1000L;
    /**
     * 是否立刻初始化,默认true
     * 开发环境由于频繁启动可以将此配置为false
     */
    private boolean init=true;
}
