package com.foco.gracefulshutdown.plugin;

import com.foco.gracefulshutdown.core.plugin.GracefulShutdownPlugin;
import com.foco.gracefulshutdown.propterties.GracefulShutdownProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import reactor.netty.DisposableServer;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: wei
 * @date：2021/12/6
 */
@Slf4j
public class NettyWebServerShutdownPlugin implements GracefulShutdownPlugin {
    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public boolean process(ApplicationContext applicationContext) {
        log.info("NettyWebServerShutdownPlugin 开始处理");
        Integer runAfterWaitTime = GracefulShutdownProperties.getConfig().getWebServer().getRunAfterWaitTime();
        try {
            log.info("NettyWebServerShutdownPlugin runAfterWaitTime 执行睡眠时间："+runAfterWaitTime);
            TimeUnit.SECONDS.sleep(runAfterWaitTime);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        if (applicationContext instanceof ReactiveWebServerApplicationContext) {

            //强制转为ReactiveWebServerApplicationContext
            ReactiveWebServerApplicationContext ctx1 = (ReactiveWebServerApplicationContext) applicationContext;
            //获取WebServer
            WebServer webServer = ctx1.getWebServer();
            //用来获取真实的server对象
            Field disposableServerField = null;
            DisposableServer disposableServer = null;
            try {
                disposableServerField = ReflectionUtils.findField(NettyWebServer.class, "disposableServer");
                disposableServerField.setAccessible(true);
                disposableServer = (DisposableServer) disposableServerField.get(webServer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(disposableServerField != null){
                    disposableServerField.setAccessible(false);
                }
            }
            if(disposableServer != null){
                //结果
                AtomicReference<GracefulShutdownResult> resultAtomicReference = new AtomicReference<>();
                webServer.shutDownGracefully((r) -> {
                    resultAtomicReference.set(r);
                    log.info("NettyWebServerShutdownPlugin 关闭WEB:" + r);
                });
                while (resultAtomicReference.get() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        log.info("NettyWebServerShutdownPlugin 睡眠等待5秒");
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }
        log.info("NettyWebServerShutdownPlugin 结束处理");
        return true;
    }

}
