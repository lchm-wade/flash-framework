package com.foco.context.core;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @Description 线程池中 线程上下文传递管理
 * @Author lucoo
 * @Date 2021/6/10 14:02
 **/
public class FocoContextManager {
    private static Collection<IContext> threadLocalTransmits = SpringContextHolder.getBeansOfType(IContext.class);

    Map<Class<? extends IContext>, Object> threadLocalValue = new HashMap<>();

    public FocoContextManager() {
        for (IContext help : threadLocalTransmits) {
            Object o = help.get();
            threadLocalValue.put(help.getClass(), o);
        }
    }

    public void set() {
        for (IContext help : threadLocalTransmits) {
            help.set(String.valueOf(threadLocalValue.get(help.getClass())));
        }
    }

    public static void remove() {
        for (IContext help : threadLocalTransmits) {
            help.remove();
        }
    }

    public static void setLocal(Function<String, String> function) {
        for (IContext contextHolder : threadLocalTransmits) {
            String focoContext = function.apply(contextHolder.buildKey(contextHolder));
            if (StrUtil.isNotBlank(focoContext)) {
                contextHolder.set(focoContext);
            }
        }
    }

    public static void setHeader(BiConsumer<String, String> consumer) {
        for (IContext contextHolder : threadLocalTransmits) {
            String context = contextHolder.get(true);
            consumer.accept(contextHolder.buildKey(contextHolder), context);
        }
    }
}
