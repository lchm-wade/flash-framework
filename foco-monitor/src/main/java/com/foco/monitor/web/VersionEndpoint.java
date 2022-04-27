package com.foco.monitor.web;

import com.foco.context.util.BootStrapPrinter;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/10 11:30
 * @since foco2.1.0
 */
@Configuration
@Endpoint(id = "version-endpoint")
public class VersionEndpoint {
    @ReadOperation
    public Map<String, String> endpoint() {
        return BootStrapPrinter.getVersionCache();
    }
}
