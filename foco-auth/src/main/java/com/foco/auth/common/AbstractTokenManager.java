package com.foco.auth.common;

import cn.hutool.core.util.StrUtil;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.util.HttpContext;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 9:37
 **/
public abstract class AbstractTokenManager implements GenericTokenManager {
    @Autowired
    private TokenProperties tokenProperties;
    public abstract void saveToken(GenericLoginContext loginContext, String token);
    public String createToken(GenericLoginContext loginContext){
        if (loginContext == null || loginContext.getUserId() == null || loginContext.getExpireTimeInSecond() == null) {
            SystemException.throwException(FocoErrorCode.PARAMS_ERROR);
        }
        String clientType = loginContext.getClientType();
        String networkType =loginContext.getNetworkType();
        if(StrUtil.isBlank(clientType)){
            clientType=HttpContext.getHeader(tokenProperties.getClientTypeHead());
            if(StrUtil.isBlank(clientType)){
                clientType="none";
            }
        }
        if(StrUtil.isBlank(networkType)){
            networkType= HttpContext.getHeader(tokenProperties.getNetworkTypeHead());
            if(StrUtil.isBlank(networkType)){
                networkType="none";
            }
        }
        String token = generateToken(loginContext.getUserId(), clientType);
        loginContext.setClientType(clientType);
        loginContext.setNetworkType(networkType);
        saveToken(loginContext,token);
        return token;
    }
}
