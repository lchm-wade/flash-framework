package com.foco.model.constant;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/5 12:38
 **/
public interface MainClassConstant {

    String FOCO_CRYPT="com.foco.crypt.autoconfigure.CryptConfiguration";
    String FOCO_INTERNATIONAL="com.foco.internation.autoconfigure.FocoInternationalAutoConfiguration";
    String FOCO_DYNAMIC_SOURCE="com.foco.boot.dynamic.source.autoconfigure.DynamicDataSourceAutoConfiguration";
    String FOCO_SHADOW="com.foco.shadow.autoconfigure.ShadowAutoConfiguration";

    String SPRING_DATA_REDIS ="org.springframework.data.redis.core.RedisTemplate";
    String SPRING_WEB_MVC="org.springframework.web.servlet.DispatcherServlet";
    String SPRING_CLOUD ="org.springframework.cloud.bootstrap.BootstrapApplicationListener";

    String SPRING_CLOUD_GATEWAY ="org.springframework.cloud.gateway.config.GatewayAutoConfiguration";

    String ALIBABA_NACOS_CONFIG="com.alibaba.cloud.nacos.NacosConfigAutoConfiguration";
    String ALIBABA_NACOS_DISCOVERY="com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration";

    String SHARDING_JDBC="io.shardingsphere.core.bootstrap.ShardingBootstrap";

    String DATA_SOURCE_AUTO_CONFIGURATION_CLASS = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration";
    String DRUID_DATA_SOURCE_AUTO_CONFIGURATION_CLASS = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure";
    String MYBATIS_PLUS="com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration";
    List<String> SWAGGER_URL= new ArrayList<String>(){{
        add("/swagger**/**");
        add("/webjars/**");
        add("/v2/**");
        add("/v3/**");
        add("/csrf/**");
        add("/error");
        add("/doc.html");
        add("/favicon.ico");
    }};
}
