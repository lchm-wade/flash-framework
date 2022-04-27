package com.foco.context.logging;

import com.foco.context.core.EnvHelper;
import com.foco.model.constant.SpringApplicationListenerOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 替换LogbackLoggingSystem为FocoLogBackLoggingSystem
 * @date 2021/12/15 08:22
 * org.springframework.boot.logging.LoggingSystem
 * @since foco2.1.0
 */
@Slf4j
public class FocoApplicationListener implements GenericApplicationListener {
    int order = Ordered.HIGHEST_PRECEDENCE + 19;
    long start_time;
    String version;
    private AtomicBoolean init = new AtomicBoolean(false);

    public FocoApplicationListener() {
        start_time = System.currentTimeMillis();
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {
            System.setProperty("org.springframework.boot.logging.LoggingSystem", "org.springframework.boot.logging.logback.FocoLogBackLoggingSystem");
        }
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
            version = FocoVersion.getVersion(this.getClass());
            Map<String, Object> map = new HashMap<>();
            map.put("foco.version", version);
            environment.getPropertySources().addFirst(new MapPropertySource("version", map));
        }
        if (event instanceof ApplicationReadyEvent) {
            if (init.get()) {
                return;
            }
            ApplicationReadyEvent applicationReadyEvent = (ApplicationReadyEvent) event;
            if (applicationReadyEvent.getApplicationContext().getParent()!=null) {
                log.info("服务启动成功,foco.version is {}", version);
                if(EnvHelper.isProd()){
                    System.out.println(String.format("Started WebApplication in %s seconds", (System.currentTimeMillis() - start_time) / 1000));
                }
            }
            init.set(true);
        }
    }

    private static final Class<?>[] EVENT_TYPES = {
            ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class,
            ApplicationReadyEvent.class};

    private static final Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return SpringApplicationListenerOrderConstants.LOGGING_ORDER;
    }

}
