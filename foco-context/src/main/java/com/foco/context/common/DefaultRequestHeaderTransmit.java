package com.foco.context.common;

import com.foco.properties.RequestHeaderTransmitProperties;
import com.foco.properties.SystemConfig;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/11/20 14:02
 */
public class DefaultRequestHeaderTransmit implements RequestHeaderTransmit {
    SystemConfig systemConfig;
    RequestHeaderTransmitProperties properties;

    public DefaultRequestHeaderTransmit(SystemConfig systemConfig, RequestHeaderTransmitProperties properties) {
        this.systemConfig = systemConfig;
        this.properties = properties;
    }

    @Override
    public List<String> transmit() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(properties.getHeaders());
        list.add(systemConfig.getApiVersionHead());
        list.add(systemConfig.getAppId());
        list.add(systemConfig.getLocaleHead());
        list.add(systemConfig.getShadowHead());
        return list;
    }
}
