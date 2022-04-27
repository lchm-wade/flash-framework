package com.foco.sharding.jdbc;

import com.foco.context.util.BootStrapPrinter;
import com.foco.context.util.PropertyUtil;
import com.foco.model.constant.MainClassConstant;
import com.foco.sharding.jdbc.properties.ConfigMapConfigurationProperties;
import com.foco.sharding.jdbc.properties.MasterSlaveRuleConfigurationProperties;
import com.foco.sharding.jdbc.properties.ShardingRuleConfigurationProperties;
import com.foco.sharding.jdbc.type.CustomLocalDateTimeTypeHandler;
import com.foco.sharding.jdbc.type.CustomLocalDateTypeHandler;
import com.google.common.base.Preconditions;
import io.shardingsphere.api.ConfigMapContext;
import io.shardingsphere.core.exception.ShardingException;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.shardingjdbc.util.DataSourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-13 13:43
 */
@Configuration
@EnableConfigurationProperties({
        ShardingRuleConfigurationProperties.class, MasterSlaveRuleConfigurationProperties.class,
        ConfigMapConfigurationProperties.class
})
@AutoConfigureBefore(name = MainClassConstant.MYBATIS_PLUS)
@Import(DataSourceHealthConfig.class)
public class ShardingAutoConfiguration implements EnvironmentAware {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-sharding-jdbc",this.getClass());
    }
    @Autowired
    private ShardingRuleConfigurationProperties shardingProperties;
    @Autowired
    private MasterSlaveRuleConfigurationProperties masterSlaveProperties;
    @Autowired
    private ConfigMapConfigurationProperties configMapProperties;
    private  Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() throws SQLException {
        if (!configMapProperties.getConfigMap().isEmpty()) {
            ConfigMapContext.getInstance().getConfigMap().putAll(configMapProperties.getConfigMap());
        }
        return null == masterSlaveProperties.getMasterDataSourceName()
                ? ShardingDataSourceFactory
                .createDataSource(dataSourceMap, shardingProperties.getShardingRuleConfiguration(), configMapProperties.getConfigMap(), configMapProperties.getProps())
                : MasterSlaveDataSourceFactory.createDataSource(
                dataSourceMap, masterSlaveProperties.getMasterSlaveRuleConfiguration(), configMapProperties.getConfigMap(), configMapProperties.getProps());
    }

    @Override
    public final void setEnvironment(final Environment environment) {
        setDataSourceMap(environment);
    }

    @SuppressWarnings("unchecked")
    private void setDataSourceMap(final Environment environment) {
        String prefix = "sharding.jdbc.datasource.";
        String dataSources = environment.getProperty(prefix + "names");
        for (String each : dataSources.split(",")) {
            try {
                Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + each.trim(), Map.class);
                Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
                DataSource dataSource = DataSourceUtil
                        .getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
                dataSourceMap.put(each, dataSource);
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingException("Can't find datasource type!", ex);
            }
        }
    }
    @Bean
    CustomLocalDateTimeTypeHandler localDateTimeTypeHandler(){
        return new CustomLocalDateTimeTypeHandler();
    }
    @Bean
    CustomLocalDateTypeHandler localDateTypeHandler(){
        return new CustomLocalDateTypeHandler();
    }
}

