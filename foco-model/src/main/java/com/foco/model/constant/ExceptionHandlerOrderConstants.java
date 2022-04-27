package com.foco.model.constant;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:56
 **/
public interface ExceptionHandlerOrderConstants {
    int WEB= FocoConstants.HIGHEST_PRECEDENCE+30;
    int FEIGN_HYSTRIX=FocoConstants.HIGHEST_PRECEDENCE+20;
    int FEIGN=FocoConstants.HIGHEST_PRECEDENCE+10;
    int HTTP=FocoConstants.HIGHEST_PRECEDENCE;
}
