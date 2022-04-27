package com.foco.auth.provider.jwt;

import com.alibaba.fastjson.JSON;
import com.foco.auth.common.GenericTokenManager;
import com.foco.auth.jwt.JwtAuthUtil;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginContext;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 15:12
 **/
@Slf4j
public class JwtTokenManager implements GenericTokenManager {
    public static final String issuer="xingyun";
    @Override
    public <T extends LoginContext> T parseToken(String key, Class<T> loginContext) {
        return JSON.parseObject(key,loginContext);
    }
    @Override
    public void refreshToken(String key, GenericLoginContext loginContext) {
        log.warn("jwt won't support refresh,nothing to do");
    }
    @Override
    public String createToken(GenericLoginContext loginContext) {
        if(loginContext == null || loginContext.getUserId() == null || loginContext.getExpireTimeInSecond()==null){
            SystemException.throwException(FocoErrorCode.PARAMS_ERROR.getCode(),"");
        }
        return JwtAuthUtil.createJWT(issuer, JSON.toJSONString(loginContext), TokenProperties.getConfig().getSecretKey(), loginContext.getExpireTimeInSecond());
    }

    @Override
    public boolean updateToken(GenericLoginContext loginContext) {
        log.warn("jwt won't support updateToken,nothing to do");
        return false;
    }

    @Override
    public boolean removeToken() {
        log.warn("jwt won't support removeToken,nothing to do");
        return false;
    }
}
