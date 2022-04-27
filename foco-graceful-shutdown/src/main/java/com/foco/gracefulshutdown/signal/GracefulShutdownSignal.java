package com.foco.gracefulshutdown.signal;

import com.foco.gracefulshutdown.core.plugin.GracefulShutdownPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.*;

/**
 * 优雅停机信号
 *
 * @author: wei
 * @date：2021/12/6
 */
@Slf4j
public class GracefulShutdownSignal implements SignalHandler {

    private static final Comparator<GracefulShutdownPlugin> comparator = new Comparator<GracefulShutdownPlugin>() {

        @Override
        public int compare(GracefulShutdownPlugin o1, GracefulShutdownPlugin o2) {
            return o1.getOrder() < o2.getOrder() ? -1 : o1.getOrder() > o2.getOrder() ? 1 : 0;
        }
    };

    private List<GracefulShutdownPlugin> gracefulShutdownPlugins = new ArrayList();

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化
     *
     * @param customPlugins
     */
    public void init(List<GracefulShutdownPlugin> customPlugins) {
        this.initCustomPlugin(customPlugins);
        this.initSpiServiceLoader();
        gracefulShutdownPlugins.sort(comparator);
    }

    /**
     * 加载定制
     *
     * @param customPlugin
     */
    private void initCustomPlugin(List<GracefulShutdownPlugin> customPlugin) {
        log.info("initCustomPlugin 开始加载GracefulShutdownPlugin");
        if (customPlugin != null && !customPlugin.isEmpty()) {
            for (GracefulShutdownPlugin gracefulShutdownPlugin : customPlugin) {
                log.info("initCustomPlugin 加载GracefulShutdownPlugin className:{},order:{}",
                        gracefulShutdownPlugin.getClass().getName(), gracefulShutdownPlugin.getOrder());
                this.addFilter(gracefulShutdownPlugin);
            }
        }
        log.info("initCustomPlugin 完成加载GracefulShutdownPlugin");
    }

    /**
     * 加载通过SPI扩展
     */
    private void initSpiServiceLoader() {
        log.info("initSpiServiceLoader 开始加载GracefulShutdownPlugin");
        ServiceLoader<GracefulShutdownPlugin> pHandlers = ServiceLoader.load(GracefulShutdownPlugin.class);
        Iterator<GracefulShutdownPlugin> iter = pHandlers.iterator();
        while (iter.hasNext()) {
            GracefulShutdownPlugin gracefulShutdownPlugin = iter.next();
            log.info("initSpiServiceLoader 加载GracefulShutdownPlugin className:{},order:{}",
                    gracefulShutdownPlugin.getClass().getName(), gracefulShutdownPlugin.getOrder());
            this.addFilter(gracefulShutdownPlugin);
        }
        log.info("initSpiServiceLoader 完成加载GracefulShutdownPlugin");
    }

    /**
     * 去除重复
     *
     * @param plugin
     */
    private void addFilter(GracefulShutdownPlugin plugin) {
        boolean exists = false;
        for (GracefulShutdownPlugin initedPlugin : this.gracefulShutdownPlugins) {
            if (initedPlugin.getClass() == plugin.getClass()) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            this.gracefulShutdownPlugins.add(plugin);
        }
    }


    /**
     * 处理信号
     *
     * @param signal
     */
    @Override
    public void handle(Signal signal) {
        log.info("优雅停机接收到：{} 信号。", signal.getName());
        boolean process = true;
        try {
            for (int i = 0; i < gracefulShutdownPlugins.size(); i++) {
                GracefulShutdownPlugin gracefulShutdownPlugin = gracefulShutdownPlugins.get(i);
                log.info("优雅停机处理链，当前处理GracefulShutdownPlugin name:{}",gracefulShutdownPlugin.getClass().getName());
                process = gracefulShutdownPlugin.process(applicationContext);
                if (!process) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("优雅停机信号处理链报错!", e);
        } finally {
            log.info("优雅停机信号处理完成.执行JVM System.exit!");
            System.exit(signal.getNumber() + 0200);
        }
    }
}
