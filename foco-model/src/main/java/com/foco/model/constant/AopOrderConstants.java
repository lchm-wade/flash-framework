package com.foco.model.constant;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/30 18:53
 **/
public interface AopOrderConstants {
    int SYS_LOG= FocoConstants.HIGHEST_PRECEDENCE+50;
    int DYNAMIC_DATTA_SOURCE=FocoConstants.HIGHEST_PRECEDENCE+40;
    int REDIS_LOCK= FocoConstants.HIGHEST_PRECEDENCE+30;
    int LIMIT_RATE= FocoConstants.HIGHEST_PRECEDENCE+20;
    int VALIDATION= FocoConstants.HIGHEST_PRECEDENCE+10;
    int PARAM_LOG= FocoConstants.HIGHEST_PRECEDENCE+1;
}
