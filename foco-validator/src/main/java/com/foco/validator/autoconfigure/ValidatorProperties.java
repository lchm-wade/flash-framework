package com.foco.validator.autoconfigure;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 默认开启
 * @date 2021/09/27 16:44
 */
@ConfigurationProperties(prefix = ValidatorProperties.PREFIX)
@Getter
@Setter
public class ValidatorProperties extends AbstractProperties {
    public static final String PREFIX=FocoConstants.CONFIG_PREFIX+"validator";
    public static ValidatorProperties getConfig(){
        return getConfig(ValidatorProperties.class);
    }
}
