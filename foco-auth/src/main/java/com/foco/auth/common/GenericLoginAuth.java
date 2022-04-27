package com.foco.auth.common;

import cn.hutool.core.util.StrUtil;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 9:19
 **/
@Slf4j
public class GenericLoginAuth extends AbstractLoginAuth {
    @Override
    public LoginContext auth(String token,String clientType,String networkType) {
        String tokenKey = tokenManager.decrypt(token);
        GenericLoginContext genericLoginContext = doAuth(tokenKey);
        if (genericLoginContext != null) {
            if (StrUtil.isBlank(clientType)) {
                clientType = "none";
            }
            if (StrUtil.isBlank(networkType)) {
                networkType = "none";
            }
            String oldNetWorkType = genericLoginContext.getNetworkType();
            if (!networkType.equals(oldNetWorkType)) {
                //同一设备 网络类型发生变化
                log.warn("用户登录网络环境发生变化当前值:{},旧中的值:{},userId:{}",networkType,oldNetWorkType,tokenKey.split(":")[0]);
                return null;
            }
            String oldClientType = genericLoginContext.getClientType();
            if (!clientType.equals(oldClientType)) {
                //token 换到不同的客户端使用
                log.warn("用户登录渠道发生变化当前值:{},旧中的值:{},,userId:{}",clientType,oldClientType,tokenKey.split(":")[0]);
                return null;
            }
            //刷新token有效期
            if (tokenProperties.isRefreshTokenPerAccess()) {
                tokenManager.refreshToken(tokenKey, genericLoginContext);
            }
        }
        return genericLoginContext;
    }
}
