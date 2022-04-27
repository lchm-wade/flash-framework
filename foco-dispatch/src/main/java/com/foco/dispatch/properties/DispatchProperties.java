package com.foco.dispatch.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:52
 **/
@Getter
@Setter
@ConfigurationProperties(prefix=DispatchProperties.PREFIX)
public class DispatchProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"dispatch";
    public static DispatchProperties getConfig(){
        return getConfig(DispatchProperties.class);
    }
    private String url;
}
