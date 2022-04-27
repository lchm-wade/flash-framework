package com.foco.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:52
 **/
@Getter
@Setter
@ConfigurationProperties(prefix=CorsProperties.PREFIX)
public class CorsProperties extends AbstractProperties {
    public static CorsProperties getConfig(){
        return getConfig(CorsProperties.class);
    }
    private boolean enabled;
    public static final String PREFIX="foco.cors";
    /**
     * 跨域配置的目标
     * 配置示例:http:www.xx.com:8080
     * 默认 *
     */
    private List<String> allowedOrigin;
    /**
     * 跨域配置的请求头,默认 *
     */
    private List<String> allowedHeader;
    /**
     * 跨域配置的暴露响应头
     * 。CORS请求时，XMLHttpRequest对象的getResponseHeader()方法只能拿到6个基本字段：
     * Cache-Control、
     * Content-Language、
     * Content-Type、
     * Expires、
     * Last-Modified、
     * Pragma。如果想拿到其他字段,
     * 就必须在Access-Control-Expose-Headers里面指定
     */
    private List<String> exposedHeaders;
    /**
     * 跨域配置的请求方法 GET,POST等,默认 *
     */
    private List<String> allowedMethod;
    /**
     * 跨域配置的url,默认/**
     */
    private String path="/**";
    /**
     *表示是否允许发送Cookie
     */
    private Boolean allowCredentials=true;
    /**
     *用来指定本次预检请求的有效期,以秒为单位 默认 1小时
     */
    private Long maxAge=36000L;
}
