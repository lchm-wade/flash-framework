package com.foco.monitor.autoconfigure;

import com.foco.monitor.web.MonitorAspect;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/20 14:34
 */
@Slf4j
@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
@ConditionalOnProperty(name = "foco.monitor.enabled")
public class MonitorWebAutoConfiguration {
    @Bean
    MonitorAspect monitorAspect(){
        return new MonitorAspect();
    }
    @Bean("costTime")
    public Gauge costTime(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("foco_request_cost_time")
                .labelNames("application","ip","path","method")
                .help("http 接口耗时").register(collectorRegistry);
    }
    @Bean("status")
    public Gauge status(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("foco_request_status")
                .labelNames("application","ip","path","method")
                .help("http 请求状态").register(collectorRegistry);
    }
    /*@Bean("trace")
    @ConditionalOnClass(name = "brave.Tracer")
    public Gauge trace(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("foco_request_trace")
                .labelNames("appName","ip","path","method")
                .help("trace信息").register(collectorRegistry);
    }
    @Bean("span")
    @ConditionalOnClass(name = "brave.Tracer")
    public Gauge span(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("foco_request_span")
                .labelNames("appName","ip","path","method")
                .help("span信息").register(collectorRegistry);
    }*/
    @Bean("startTime")
    public Gauge startTime(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("foco_request_start_time")
                .labelNames("application","ip","path","method")
                .help("开始时间").register(collectorRegistry);
    }
    /*@Bean
    @ConditionalOnClass(name = "brave.Tracer")
    public Gauge httpRequestDetailTrace(CollectorRegistry collectorRegistry){
        return  Gauge.build()
                .name("http_request_cost_time")
                .labelNames("appName", "traceId", "spanId", "parentSpanId",
                        "ip","path","method","code")
                .help("http 接口详情").register(collectorRegistry);
    }*/
}
