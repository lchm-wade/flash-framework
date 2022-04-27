package com.foco.properties;

import lombok.Data;

/**
 * @author ChenMing
 * @date 2021/11/16
 */
@Data
public class DiscoveryPublicProperties {
    /**
     * 默认route
     */
    public static final String DEFAULT_ROUTE = "default";

    /**
     * 机器标识
     */
    protected String route = DEFAULT_ROUTE;
}
