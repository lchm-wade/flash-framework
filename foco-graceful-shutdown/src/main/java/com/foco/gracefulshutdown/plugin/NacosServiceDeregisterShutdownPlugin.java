package com.foco.gracefulshutdown.plugin;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.foco.gracefulshutdown.core.plugin.GracefulShutdownPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author: wei
 * @date：2021/12/6
 */
@Slf4j
public class NacosServiceDeregisterShutdownPlugin implements GracefulShutdownPlugin {

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public boolean process(ApplicationContext applicationContext) {
        log.info("NacosServiceDeregister 开始处理");
        NacosAutoServiceRegistration bean = applicationContext.getBeanProvider(NacosAutoServiceRegistration.class)
                .getIfAvailable();
        if(bean != null){
            Method deregister = ReflectionUtils.findMethod(NacosAutoServiceRegistration.class, "deregister");
            try {
                deregister.setAccessible(true);
                deregister.invoke(bean);
                log.info("NacosServiceDeregister 已取消注册节点");
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                deregister.setAccessible(false);
            }
        }
        log.info("NacosServiceDeregister 结束处理");
        return true;
    }
}
