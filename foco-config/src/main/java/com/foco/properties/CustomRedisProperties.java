package com.foco.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description----------
 *
 * @Author lucoo
 * @Date 2021/6/26 13:59
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class CustomRedisProperties {
    /**
     * redis key前缀,一般用项目名做区分，数据隔离
     */
    private String redisKeyPrefix="";
}