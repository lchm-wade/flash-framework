package com.foco.sharding.jdbc.properties;

import io.shardingsphere.core.yaml.masterslave.YamlMasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-13 13:41
 */
@ConfigurationProperties(prefix = "sharding.jdbc.config.masterslave")
public class MasterSlaveRuleConfigurationProperties extends YamlMasterSlaveRuleConfiguration {
}
