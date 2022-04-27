package com.foco.http.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/29 18:14
 */
@Getter
@Setter
@ConfigurationProperties(prefix=HttpProperties.PREFIX)
public class HttpProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"http";
    public static HttpProperties getConfig(){
        return getConfig(HttpProperties.class);
    }
    /**
     * 扫包路径
     */
    private String basePackage;
}

