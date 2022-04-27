package com.foco.model.spi;

import com.foco.model.ApiResult;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 11:34
 */
public interface TraceIdBuilder {
    void buildTraceId(ApiResult result);
}
