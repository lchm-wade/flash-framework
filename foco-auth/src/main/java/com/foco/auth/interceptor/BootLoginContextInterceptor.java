package com.foco.auth.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.foco.auth.properties.TokenProperties;
import com.foco.properties.SystemConfig;
import com.foco.context.core.LoginAuth;
import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.HttpContext;
import com.foco.context.util.PathMatchUtil;
import com.foco.context.util.ResponseUtils;
import com.foco.model.ApiResult;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.constant.MainClassConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web 请求拦截器
 *
 * @Author lucoo
 * @Date 2021/6/26 14:55
 */
@Slf4j
public class BootLoginContextInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LoginAuth loginAuth;
    @Autowired
    private TokenProperties tokenProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            if (request.getMethod().equals(HttpMethod.OPTIONS.name())||!tokenProperties.isEnabled()||
                    PathMatchUtil.match(MainClassConstant.SWAGGER_URL, request.getServletPath())) {
                return true;
            }
            if (request != null) {
                if (CollectionUtil.isNotEmpty(tokenProperties.getUrlList())) {
                    boolean matching = PathMatchUtil.match(tokenProperties.getUrlList(),request.getServletPath());//tokenProperties.getUrlList().contains(request.getServletPath());
                    if (matching&&tokenProperties.getIsWhite()) {
                        return true;
                    }else if (!matching&&!tokenProperties.getIsWhite()) {
                        return true;
                    }
                }
                String token = getFromHeaderAndCookie(request, SystemConfig.getConfig().getTokenHead());
                if (StrUtil.isBlank(token)) {
                    ResponseUtils.write(HttpStatus.OK.value(), JSON.toJSONString(ApiResult.error(FocoErrorCode.UNAUTHORIZED)));
                    return false;
                }
                String clientType = HttpContext.getHeader(tokenProperties.getClientTypeHead());
                String networkType = HttpContext.getHeader(tokenProperties.getNetworkTypeHead());
                LoginContext loginContext = loginAuth.auth(token, clientType, networkType);
                if (loginContext == null) {
                    ResponseUtils.write(HttpStatus.OK.value(), JSON.toJSONString(ApiResult.error(FocoErrorCode.UNAUTHORIZED)));
                    return false;
                }
                LoginContextHolder.set(loginContext);
            }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LoginContextHolder.remove();
    }

    public String getFromHeaderAndCookie(HttpServletRequest request, String key) {
        String tokenVal = request.getHeader(key);
        if (StrUtil.isBlank(tokenVal)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (key.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return tokenVal;
    }
}