package com.foco.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/26 11:19
 **/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = SystemConfig.PREFIX)
@EnableConfigurationProperties({CorsProperties.class,CustomRedisProperties.class, RequestHeaderTransmitProperties.class})
public class SystemConfig extends AbstractProperties{
    public static final String PREFIX="foco";
    public static SystemConfig getConfig(){
        return getConfig(SystemConfig.class);
    }
    /**
     * 语言 请求头
     */
    private String localeHead="locale";
    /**
     * token 请求头
     */
    private String tokenHead="token";

    /**
     * 应用id 请求头
     */
    private String appId="appId";
    private String apiVersionHead="Api-Version";
    /**
     * 影子库请求头
     * 示例:shadow=true
     */
    private String shadowHead="shadow";
    /**
     * 环境标识
     * 优化获取foco.env的配置
     * 若没有则获取
     * spring.profiles.active的配置
     * 环境统一标识
     * 开发环境:dev
     * 测试环境:test
     * fix环境：fix
     * 生产环境：prod
     */
    private String env;
}
