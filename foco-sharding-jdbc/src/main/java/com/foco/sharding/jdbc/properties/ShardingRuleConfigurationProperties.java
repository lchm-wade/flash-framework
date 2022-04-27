package com.foco.sharding.jdbc.properties;

import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-13 13:42
 */
@ConfigurationProperties(prefix = "sharding.jdbc.config.sharding")
public class ShardingRuleConfigurationProperties extends YamlShardingRuleConfiguration {
}

