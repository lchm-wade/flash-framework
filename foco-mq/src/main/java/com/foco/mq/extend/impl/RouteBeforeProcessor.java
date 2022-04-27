package com.foco.mq.extend.impl;

import com.foco.model.constant.FocoConstants;
import com.foco.mq.constant.MqConstant;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.extend.ConsumeBeforeProcessorSkip;
import com.foco.mq.extend.ProductionBeforeProcessor;
import com.foco.mq.model.Msg;
import com.foco.mq.properties.MqProperties;
import com.foco.properties.DiscoveryPublicProperties;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author ChenMing
 * @date 2021/11/5
 */
public class RouteBeforeProcessor implements ProductionBeforeProcessor, ConsumeBeforeProcessorSkip {

    @Resource
    private DiscoveryPublicProperties discoveryProperties;

    @Resource
    private MqProperties mqProperties;

    public String getRoute() {
        return getDefaultRoute().equals(getLocalRoute()) ? "" : getLocalRoute();
    }

    public String getLocalRoute() {
        return discoveryProperties.getRoute();
    }

    public String getDefaultRoute() {
        return FocoConstants.DEFAULT_ROUTE;
    }

    @Override
    public boolean postProcessBeforeConsumeSkip(Msg msg) {
        String route = msg.getProperties().get(MsgPropertyConstant.ROUTE);
        if (StringUtils.isEmpty(route) || !mqProperties.isLabelRoute()) {
            return false;
        }
        return !getLocalRoute().equalsIgnoreCase(route);
    }

    @Override
    public Msg postProcessBeforeProduction(Msg msg) {
        msg.put(MsgPropertyConstant.ROUTE, discoveryProperties.getRoute());
        return msg;
    }

    @Override
    public int consumeOrder() {
        return MqConstant.ROUTE_BEFORE_PROCESSOR_WRAP_ORDER;
    }
}
