package com.foco.auth.provider.jwt;

import com.foco.auth.common.AbstractLoginAuth;
import com.foco.auth.jwt.JwtAuthUtil;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.core.LoginContext;
import io.jsonwebtoken.Claims;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/12 11:28
 **/
public class JwtLoginAuth extends AbstractLoginAuth {
    @Override
    public LoginContext auth(String token, String clientType, String networkType) {
        Claims claims = JwtAuthUtil.parseJWT(JwtTokenManager.issuer, token, TokenProperties.getConfig().getSecretKey());
        if(claims!=null){
            return doAuth(claims.getSubject());
        }
        return null;
    }
}
