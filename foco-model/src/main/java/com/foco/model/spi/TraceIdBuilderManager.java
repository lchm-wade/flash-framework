package com.foco.model.spi;

import com.foco.model.ApiResult;

import java.util.ServiceLoader;

public class TraceIdBuilderManager {
    private static TraceIdBuilder traceIdBuilder;
    static {
        if(traceIdBuilder==null){
            synchronized (TraceIdBuilderManager.class){
                if(traceIdBuilder==null){
                    ServiceLoader<TraceIdBuilder> traceIdBuilders = ServiceLoader.load(TraceIdBuilder.class);
                    for(TraceIdBuilder builder:traceIdBuilders){
                        traceIdBuilder=builder;
                        break;
                    }
                }
            }
        }
    }
    public static void buildTraceId(ApiResult result){
        if(traceIdBuilder!=null){
            traceIdBuilder.buildTraceId(result);
        }
    }

}
