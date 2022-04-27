package com.foco.sharding.jdbc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-13 13:39
 */
@ConfigurationProperties(prefix = "sharding.jdbc.config")
@Getter
@Setter
public class ConfigMapConfigurationProperties {
    private Map<String, Object> configMap = new LinkedHashMap<>();
    private Properties props = new Properties();
}