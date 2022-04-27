package com.foco.monitor.autoconfigure;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author: wei
 * @date：2022/1/13
 */
@ManagementContextConfiguration(ManagementContextType.CHILD)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletManagementChildCustomConfiguration {

    @Bean
    @ConditionalOnClass({ Tomcat.class, UpgradeProtocol.class })
    @Order
    public WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> managementTomcatWebServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory>(){

            @Override
            public void customize(ConfigurableTomcatWebServerFactory factory) {
                factory.addConnectorCustomizers((connector) -> {
                    ProtocolHandler handler = connector.getProtocolHandler();
                    if (handler instanceof AbstractProtocol) {
                        AbstractProtocol protocol = (AbstractProtocol) handler;
                        protocol.setMaxThreads(10);
                        protocol.setMinSpareThreads(10);
                    }
                });
            }
        };
    }
}
