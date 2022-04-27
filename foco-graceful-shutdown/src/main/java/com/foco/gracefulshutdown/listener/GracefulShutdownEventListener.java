package com.foco.gracefulshutdown.listener;

import com.foco.gracefulshutdown.core.plugin.GracefulShutdownPlugin;
import com.foco.gracefulshutdown.signal.GracefulShutdownSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import sun.misc.Signal;

import java.util.List;

@Slf4j
public class GracefulShutdownEventListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Autowired(required = false)
    private List<GracefulShutdownPlugin> customPlugins;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("GracefulShutdownEventListener onApplicationEvent 配置监听!");
        GracefulShutdownSignal gracefulShutdownSignal = new GracefulShutdownSignal();
        gracefulShutdownSignal.setApplicationContext(applicationReadyEvent.getApplicationContext());
        gracefulShutdownSignal.init(customPlugins);
        Signal.handle(new Signal("TERM"), gracefulShutdownSignal);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}