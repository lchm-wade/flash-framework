package com.foco.gracefulshutdown.propterties;

import com.foco.model.constant.FocoConstants;
import com.foco.properties.AbstractProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/13 10:36
 */
@ConfigurationProperties(prefix = GracefulShutdownProperties.PREFIX)
@Data
public class GracefulShutdownProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"graceful-shutdown";

    private boolean enabled=true;

    private final WebServer webServer = new WebServer();

    @Data
    public static class WebServer{

        /**
         * web server 等待运行时间.
         */
        private Integer runAfterWaitTime = 15;
    }


    public static GracefulShutdownProperties getConfig(){
        return getConfig(GracefulShutdownProperties.class);
    }

}
