package com.foco.gracefulshutdown;


import com.foco.context.util.BootStrapPrinter;
import com.foco.gracefulshutdown.listener.GracefulShutdownEventListener;
import com.foco.gracefulshutdown.plugin.NacosServiceDeregisterShutdownPlugin;
import com.foco.gracefulshutdown.plugin.NettyWebServerShutdownPlugin;
import com.foco.gracefulshutdown.plugin.TomcatWebServerShutdownPlugin;
import com.foco.gracefulshutdown.propterties.GracefulShutdownProperties;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 优雅关机自动配置类
 * @author: wei
 * @date：2021/12/7
 */
@Configuration
@ConditionalOnProperty(prefix= GracefulShutdownProperties.PREFIX,name = "enabled",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties(GracefulShutdownProperties.class)
public class GracefulShutdownAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-graceful-shutdown",this.getClass());
    }

    @Bean
    public GracefulShutdownEventListener GracefulShutdownEventListener(){
        return new GracefulShutdownEventListener();
    }

    @Bean
    @ConditionalOnClass(name = {
            "org.springframework.web.servlet.DispatcherServlet",
            "org.apache.catalina.startup.Tomcat"})
    public TomcatWebServerShutdownPlugin tomcatWebServerShutdownPlugin(){
        return new TomcatWebServerShutdownPlugin();
    }

    @Bean
    @ConditionalOnClass(name = {
            "org.springframework.web.reactive.DispatcherHandler",
            "reactor.netty.http.server.HttpServer",
            "reactor.netty.DisposableServer"})
    public NettyWebServerShutdownPlugin nettyWebServerShutdownPlugin(){
        return new NettyWebServerShutdownPlugin();
    }

    @Bean
    @ConditionalOnClass(name = MainClassConstant.ALIBABA_NACOS_DISCOVERY)
    public NacosServiceDeregisterShutdownPlugin nacosServiceDeregisterShutdownPlugin(){
        return new NacosServiceDeregisterShutdownPlugin();
    }
}