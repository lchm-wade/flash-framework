package com.foco.monitor.web;

import io.prometheus.client.Gauge;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @Author lucoo
 * @Date 2021/10/20 14:25
 */
@Slf4j
@Aspect
@Order(-2147483648)
public class MonitorAspect{
    @Autowired
    @Qualifier("costTime")
    private Gauge costTime;
    @Autowired
    @Qualifier("status")
    private Gauge status;
    @Autowired
    @Qualifier("startTime")
    private Gauge startTime;


    @Value("${spring.application.name}")
    private String appName;
    private final String HTTP_STATUS_SUCCESS="200";
    private final String HTTP_STATUS_ERROR="500";
    @Around(value = "@within(org.springframework.web.bind.annotation.RestController)")
    public Object observer(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();
        long startTime = System.currentTimeMillis();
        String code=HTTP_STATUS_SUCCESS;
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            code=HTTP_STATUS_ERROR;
            throw throwable;
        }finally {
            long endTime = System.currentTimeMillis();
            report(request,code,endTime-startTime,startTime);
        }
    }
    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
    private void report(HttpServletRequest request,String code,long costTimeMs,long startTimeMs){
        String ip=IpUtil.getLocalIpAddress();
        String path=request.getServletPath();
        String method=request.getMethod();
        status.labels(appName,ip,path, method).set(Double.valueOf(code));
        costTime.labels(appName,ip,path, method).set(Double.valueOf(costTimeMs));
        startTime.labels(appName,ip,path, method).set(startTimeMs);
    }
}
