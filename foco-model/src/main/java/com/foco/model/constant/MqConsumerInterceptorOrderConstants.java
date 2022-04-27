package com.foco.model.constant;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-18 14:48
 */
public interface MqConsumerInterceptorOrderConstants {
    int CONSUMER_LOG= FocoConstants.HIGHEST_PRECEDENCE;
    int MSG_IDEMPOTENT=FocoConstants.HIGHEST_PRECEDENCE+10;
}
