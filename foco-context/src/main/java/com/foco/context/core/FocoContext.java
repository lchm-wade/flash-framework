package com.foco.context.core;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import java.util.Optional;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 暴露给业务端使用的，全链路参数透传
 * 业务方自己写一个类集成该类作为工具类
 * @date 2021/11/17 18:02
 */
public abstract class FocoContext implements IContext {
    ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

    public <T> T getContext(Class<T> type) {
        String context = Optional.ofNullable(contextHolder.get())
                .orElse(null);
        context = UnicodeUtil.toString(context);
        return StrUtil.isBlank(context) ? null : JSON.parseObject(context, type);
    }

    public <T> void setContext(T t) {
        set(JSON.toJSONString(t));
    }

    @Override
    public void remove() {
        contextHolder.remove();
    }

    @Override
    public String get() {
        return get(false);
    }

    @Override
    public String get(Boolean direct) {
        if (direct) {
            return contextHolder.get();
        }
        return UnicodeUtil.toString(contextHolder.get());
    }

    @Override
    public void set(String context) {
        contextHolder.set(UnicodeUtil.toUnicode(context));
    }
}
