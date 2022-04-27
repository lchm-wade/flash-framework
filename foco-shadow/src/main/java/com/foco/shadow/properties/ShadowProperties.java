package com.foco.shadow.properties;

import com.foco.model.constant.FocoConstants;
import com.foco.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/2 14:43
 **/
@ConfigurationProperties(prefix = ShadowProperties.PREFIX)
@Getter
@Setter
public class ShadowProperties extends AbstractProperties {
    public boolean enabled=true;
    /**影子表标志*/
    public String flag="shadow";
    /**忽略的影子表*/
    private Set<String> ignoreShadowTableNames=new HashSet<>();
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"shadow";
    public static ShadowProperties getConfig(){
        return getConfig(ShadowProperties.class);
    }
}
