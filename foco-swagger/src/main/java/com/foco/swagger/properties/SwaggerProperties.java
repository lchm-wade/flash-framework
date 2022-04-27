package com.foco.swagger.properties;

import com.foco.properties.AbstractProperties;
import com.foco.properties.SystemConfig;
import com.foco.model.constant.FocoConstants;
import com.foco.swagger.header.GlobalOperationParameter;
import com.foco.swagger.header.SwaggerHeaderParameter;
import lombok.Data;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * description----------
 *
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Data
@ConfigurationProperties(prefix = SwaggerProperties.PREFIX)
public class SwaggerProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"swagger";
    public static SwaggerProperties getConfig(){
        return getConfig(SwaggerProperties.class);
    }
    private boolean enabled=false;
    private String title="REST API接口在线文档";
}
