package com.foco.monitor.bootstrap;

import com.foco.context.util.BootStrapPrinter;
import com.foco.monitor.autoconfigure.MonitorWebAutoConfiguration;
import com.foco.monitor.web.VersionEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Import({MonitorWebAutoConfiguration.class, VersionEndpoint.class})
@Slf4j
public class MonitorBootStrap {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-monitor",this.getClass());
    }
}
