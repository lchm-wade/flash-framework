package com.foco.gracefulshutdown.plugin;

import com.foco.gracefulshutdown.core.plugin.GracefulShutdownPlugin;
import com.foco.gracefulshutdown.propterties.GracefulShutdownProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: wei
 * @date：2021/12/6
 */
@Slf4j
public class TomcatWebServerShutdownPlugin implements GracefulShutdownPlugin {

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public boolean process(ApplicationContext applicationContext) {
        log.info("TomcatWebServerShutdownPlugin 开始处理");
        Integer runAfterWaitTime = GracefulShutdownProperties.getConfig().getWebServer().getRunAfterWaitTime();
        try {
            log.info("TomcatWebServerShutdownPlugin runAfterWaitTime 执行睡眠时间："+runAfterWaitTime);
            TimeUnit.SECONDS.sleep(runAfterWaitTime);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        if (applicationContext instanceof ServletWebServerApplicationContext) {
            AtomicReference<GracefulShutdownResult> resultAtomicReference = new AtomicReference<>();

            WebServerApplicationContext ctx1 = (WebServerApplicationContext) applicationContext;

            ctx1.getWebServer().shutDownGracefully((r) -> {
                resultAtomicReference.set(r);
                log.info("TomcatWebServerShutdownPlugin 关闭WEB:"+r);
            });
            while (resultAtomicReference.get() == null) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    log.info("TomcatWebServerShutdownPlugin 睡眠等待5秒");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        log.info("TomcatWebServerShutdownPlugin 结束处理");
        return true;
    }
}
