package com.foco.model.constant;

/**
 * @author 程昭斌
 * @version 1.0
 * @description: 网关过滤器执行顺序
 * @date 2019/8/27 16:28
 */
public class GatewayOrdersConstants {


    public static final int LOAD_BALANCER_SWEEPER = Integer.MIN_VALUE;

    /**
     * NettyWriteResponseFilter的order值为-1，
     * 我们需要覆盖返回响应体的逻辑，自定义的GlobalFilter必须比NettyWriteResponseFilter优先执行
     */
    public static final int REQUEST_FILTER = -310000;
    public static final int MONITOR_FILTER = -300000;
    public static final int AUTH_FILTER = -290000;
    public static final int GRAY_FILTER = -280000;

    public static final int PERMISSIONS_FILTER = -270000;
    public static final int DECRYPT_FILTER = -260000;
    public static final int CONTEXT_FILTER = Integer.MAX_VALUE - 1;

    /**
     * 等于org.springframework.cloud.gateway.filter.LoadBalancerClientFilter#LOAD_BALANCER_CLIENT_FILTER_ORDER - 1
     */
    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10100 - 1;
    public static final int LOAD_HEADER_FILTER_ORDER = CONTEXT_FILTER - 1;

}
