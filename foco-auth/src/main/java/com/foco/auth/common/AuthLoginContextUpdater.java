package com.foco.auth.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.foco.auth.properties.TokenProperties;
import com.foco.properties.SystemConfig;
import com.foco.context.core.*;
import com.foco.context.util.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/29 16:33
 */
public class AuthLoginContextUpdater implements LoginContextUpdater {
    @Autowired
    private LoginAuth loginAuth;
    @Override
    public void update() {
        TokenProperties config = TokenProperties.getConfig();
        String token = HttpContext.getHeader(SystemConfig.getConfig().getTokenHead());
        if (StrUtil.isNotBlank(token)) {
            String clientType = HttpContext.getHeader(config.getClientTypeHead());
            String networkType = HttpContext.getHeader(config.getNetworkTypeHead());
            LoginContext loginContext = loginAuth.auth(token, clientType, networkType);
            String context = loginContext == null ? null : JSON.toJSONString(loginContext);
            LoginContextHolder.set(context);
        }
    }
}
