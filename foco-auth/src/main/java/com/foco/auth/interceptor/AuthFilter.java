package com.foco.auth.interceptor;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.annotation.GatewayTrace;
import com.foco.context.core.LoginContextHolder;
import com.foco.properties.SystemConfig;
import com.foco.context.core.LoginAuth;
import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextConstant;
import com.foco.context.util.PathMatchUtil;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.constant.GatewayOrdersConstants;
import com.foco.model.constant.MainClassConstant;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p> BaseFilter </p>
 *
 * @author 程昭斌
 * @version 1.0
 * @date 2021/6/7 15:53
 */
@Slf4j
@GatewayTrace
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private LoginAuth loginAuth;
    /**
     * @return 若检验成功则返回用户信息, 否则返回null
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isSwagger(request.getPath().toString())
                || (request.getMethod() != null && request.getMethod().name().equals(HttpMethod.OPTIONS.name()))
                || !tokenProperties.isEnabled()) {
            return chain.filter(exchange);
        }
        boolean matching = isWhitelist(request);
        if (matching && tokenProperties.getIsWhite()) {
            return chain.filter(exchange);
        } else if (!matching && !tokenProperties.getIsWhite()) {
            return chain.filter(exchange);
        }
        String token = getFromHeaderAndCookie(exchange.getRequest(), SystemConfig.getConfig().getTokenHead());
        if (StrUtil.isBlank(token)) {
            SystemException.throwException(FocoErrorCode.UNAUTHORIZED);
        }
        String clientType = exchange.getRequest().getHeaders().getFirst(tokenProperties.getClientTypeHead());
        String networkType = exchange.getRequest().getHeaders().getFirst(tokenProperties.getNetworkTypeHead());
        LoginContext loginContext = loginAuth.auth(token, clientType, networkType);
        if (loginContext == null) {
            SystemException.throwException(FocoErrorCode.UNAUTHORIZED);
        }
        String headerValues = UnicodeUtil.toUnicode(JSON.toJSONString(loginContext));

        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(LoginContextConstant.LOGIN_CONTEXT, headerValues).build();
        //构建新的ServerWebExchange实例
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        //当前用户信息放入上下文
        LoginContextHolder.set(loginContext);
        return chain.filter(newExchange);
    }

    /**
     * 如果业务方的服务前缀是形如 /demo/order，可以重写该方法去跳过
     * @param path
     * @return
     */
    protected boolean isSwagger(String path) {
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(path, "/")).skip(1).collect(Collectors.joining("/"));
        newPath = newPath + (newPath.length() > 1 && path.endsWith("/") ? "/" : "");
        return PathMatchUtil.match(MainClassConstant.SWAGGER_URL, newPath);
    }

    /**
     * 白名单校验
     */
    public boolean isWhitelist(ServerHttpRequest request) {
        String path = request.getPath().toString();
        if (StrUtil.isNotBlank(path)) {
            List<String> urls = tokenProperties.getUrlList();
            return PathMatchUtil.match(urls, path);
        }
        log.debug("path:{}, whitelist:{}", path, true);
        return true;
    }

    /***从当前请求（请求头/cookie）获取授权参数*/
    public String getFromHeaderAndCookie(ServerHttpRequest request, String key) {
        HttpHeaders headers = request.getHeaders();
        // 1.从请求头中获取
        String paramValue = headers.getFirst(key);
        if (StrUtil.isBlank(paramValue)) {
            // 2.从cookie中获取
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            HttpCookie cookie = cookies.getFirst(key);
            paramValue = Optional.ofNullable(cookie).map(HttpCookie::getValue).orElse(null);
        }
        return paramValue;
    }

    @Override
    public int getOrder() {
        return GatewayOrdersConstants.AUTH_FILTER;
    }
}
