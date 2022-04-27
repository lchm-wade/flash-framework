package com.foco.context.util;

import cn.hutool.core.util.StrUtil;
import com.foco.context.common.RequestHeaderTransmit;
import com.foco.context.core.SpringContextHolder;
import com.foco.model.constant.FocoConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> HttpContext </p>
 *
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
public class HttpContext {
    private static final ThreadLocal<Map<String, String>> HEADERS = ThreadLocal.withInitial(ConcurrentHashMap::new);
    private static List<String> transmitHeaders = new ArrayList<>();

    public static boolean isFeignRequest() {
        return FocoConstants.FEIGN_ORIGINAL.equals(getHeader(FocoConstants.ORIGINAL));
    }

    public static boolean isNotFeignRequest() {
        return !isFeignRequest();
    }

    public static String getHeader(String headName) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String header = request.getHeader(headName);
            if (!StringUtils.isEmpty(header)) {
                return header;
            }
        }
        return HEADERS.get().get(headName);
    }

    public static void setHeaders(Map<String, String> headers) {
        HEADERS.set(headers);
    }

    public static void cleanHeaders() {
        HEADERS.remove();
    }

    public static Map<String, String> getHeaders() {
        HttpServletRequest request = getRequest();
        Map<String, String> result = new HashMap<>();
        if (request != null) {
            if (transmitHeaders.isEmpty()) {
                synchronized (HttpContext.class){
                    if(transmitHeaders.isEmpty()){
                        Collection<RequestHeaderTransmit> requestHeaderTransmits = SpringContextHolder.getBeansOfType(RequestHeaderTransmit.class);
                        for(RequestHeaderTransmit requestHeaderTransmit:requestHeaderTransmits){
                            transmitHeaders.addAll(requestHeaderTransmit.transmit());
                        }
                    }
                }
            }
            for (String header : transmitHeaders) {
                result.put(header, request.getHeader(header));
            }
            return result;
        } else {
            return HEADERS.get();
        }
    }

    /**
     * 获取当前请求的Request URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getRequestURL().toString();
        }
        return "";
    }

    public static String getIp() {
        String ip = getHeader("x-forwarded-for");
        if (StrUtil.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            HttpServletRequest request = getRequest();
            if (request != null) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static Map<String, String> getRequestParameters() {
        HashMap<String, String> values = new HashMap<>();
        HttpServletRequest request = HttpContext.getRequest();
        if (request == null) {
            return values;
        }
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }
        return values;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getResponse();
    }

    public static void removeRequestAttributes() {
        RequestContextHolder.resetRequestAttributes();
    }

    public static void setRequestAttributes(RequestAttributes request) {
        RequestContextHolder.setRequestAttributes(request, true);
    }

    public static RequestAttributes getRequestAttributes() {
        return RequestContextHolder.getRequestAttributes();
    }
}
