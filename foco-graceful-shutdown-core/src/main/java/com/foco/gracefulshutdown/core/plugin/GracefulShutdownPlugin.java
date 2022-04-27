package com.foco.gracefulshutdown.core.plugin;

import org.springframework.context.ApplicationContext;

/**
 * @author: wei
 * @date：2021/12/6
 */
public interface GracefulShutdownPlugin {
    /**
     * 排序使用，值越小，优先级越高.
     * @return
     */
    int getOrder();

    /**
     * 具体处理
     * @param applicationContext
     * @return
     */
    boolean process(ApplicationContext applicationContext);
}
