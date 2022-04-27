package com.foco.page.mvc.interceptor;

import cn.hutool.core.util.StrUtil;
import com.foco.context.util.HttpContext;
import com.foco.model.constant.FocoConstants;
import com.foco.model.page.ThreadPagingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分页处理拦截器
 *
 * @Author lucoo
 * @Date 2021/6/23 18:55
 */
public class WebPageInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String pageJson = HttpContext.getHeader(FocoConstants.PAGE);
        if (StrUtil.isNotEmpty(pageJson)) {
            ThreadPagingUtil.set(pageJson);
        }
        ThreadPagingUtil.buildPageParam(request.getParameter(FocoConstants.PAGE_CURRENT),request.getParameter(FocoConstants.PAGE_SIZE));
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadPagingUtil.clear();
    }
}
