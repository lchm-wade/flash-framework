package com.foco.auth.common;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginContext;

import java.util.Random;

public interface GenericTokenManager extends TokenManager{
    /**
     * 支持 32字节的字符串
     */
    String SEPARATOR = ":";

    default String generateToken(String userId, String clientType) {
        AES aes = SecureUtil.aes(TokenProperties.getConfig().getSecretKey().getBytes());
        String content = userId + SEPARATOR + clientType + SEPARATOR + System.currentTimeMillis() + SEPARATOR + new Random().nextInt(10000);
        return aes.encryptHex(content);
    }
    default String buildKey(String userId, String clientType,String token){
        return userId + SEPARATOR + clientType + SEPARATOR +token;
    }
    default String decrypt(String token) {
        AES aes = SecureUtil.aes(TokenProperties.getConfig().getSecretKey().getBytes());
        String decryptStr;
        try {
            decryptStr = aes.decryptStr(token, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            return "0:0:0";
        }
        String[] split = decryptStr.split(SEPARATOR);
        return split[0] + SEPARATOR + split[1] + SEPARATOR + token;
    }

    <T extends LoginContext> T parseToken(String key, Class<T> loginContext);

    void refreshToken(String key, GenericLoginContext loginContext);
}
