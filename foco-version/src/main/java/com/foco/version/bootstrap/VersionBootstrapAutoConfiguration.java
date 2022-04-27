package com.foco.version.bootstrap;


import com.foco.context.util.BootStrapPrinter;
import com.foco.version.autoconfigure.CustomWebMvcRegistrations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/23 17:14
 **/
@Slf4j
@Import({
        CustomWebMvcRegistrations.class
})
public class VersionBootstrapAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-version",this.getClass());
    }
}
