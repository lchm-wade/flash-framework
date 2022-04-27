package com.foco.syslog.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:52
 **/
@Getter
@Setter
@ConfigurationProperties(prefix=SysLogProperties.PREFIX)
public class SysLogProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"syslog";
    public static SysLogProperties getConfig(){
        return getConfig(SysLogProperties.class);
    }
    /**
     * 是否启用 默认启用
     */
    private boolean enabled=true;
    /**
     * 日志存储介质 默认db
     * db
     * mongo
     * custom 用户自定义存储实现
     */
    private LogStoreEnum store= LogStoreEnum.db;
    public enum LogStoreEnum{
        db,mongo,custom;
    }
}
