package com.foco.model.constant;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/30 18:53
 **/
public interface WebInterceptorOrderConstants {
    int PAGE_INTERCEPTOR = FocoConstants.HIGHEST_PRECEDENCE + 40;
    int SHADOW_INTERCEPTOR=FocoConstants.HIGHEST_PRECEDENCE + 30;
    int PERMISSION = FocoConstants.HIGHEST_PRECEDENCE + 20;
    int AUTH_INTERCEPTOR = FocoConstants.HIGHEST_PRECEDENCE+10;
    int CLOUD_INTERCEPTOR = FocoConstants.HIGHEST_PRECEDENCE;
}
