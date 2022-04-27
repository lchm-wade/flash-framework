package com.foco.context.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2022/03/10 14:50
 * @since foco2.1.0
 */
public class CryptUtil {
    private static final String SECRET_KEY="0gc4d86q134SR34gwer90a2dgfujvf2w";

    public static String crypt(String key,String content){
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.encryptBase64(content);
    }
    public static String deCrypt(String key,String content){
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.decryptStr(content);
    }

    public static String crypt(String content){
        return crypt(SECRET_KEY,content);
    }
    public static String deCrypt(String content){
        return deCrypt(SECRET_KEY,content);
    }
    public static String encodeByBase64(String content){
        return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }
    public static String decodeByBase64(String content){
     return new String(Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
    }
}
