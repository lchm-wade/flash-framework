package com.foco.internation.bootstrap;

import com.foco.context.util.BootStrapPrinter;
import com.foco.internation.autoconfigure.LocalInternationalAutoConfiguration;
import com.foco.internation.autoconfigure.NacosInternationAutoConfiguration;
import com.foco.internation.properties.InternationalProperties;
import com.foco.model.constant.FocoConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/15 10:52
 */
@Import({LocalInternationalAutoConfiguration.class, NacosInternationAutoConfiguration.class})
@ConditionalOnProperty(prefix= InternationalProperties.PREFIX,name = FocoConstants.ENABLED,matchIfMissing = true)
@EnableConfigurationProperties(InternationalProperties.class)
public class InternationalBootStrap {
    @PostConstruct
    public void init(){
        BootStrapPrinter.log("foco-international",this.getClass());
    }
}
