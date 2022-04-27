package com.foco.crypt.core.algorithm.crypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.algorithm.Crypt;

/**
 * @Description Aes 加解密
 * @Author lucoo
 * @Date 2021/6/15 14:21
 **/
@Crypt(algorithm=CryptProperties.Algorithm.AES)
public class AesCryptAlgorithm implements CryptAlgorithm {
    @Override
    public String deCrypt(CryptProperties cryptProperties,String content){
        AES aes = SecureUtil.aes(cryptProperties.getCryptKey().getBytes());
        return aes.decryptStr(content);
    }

    @Override
    public String enCrypt(CryptProperties cryptProperties,String content){
        AES aes = SecureUtil.aes(cryptProperties.getCryptKey().getBytes());
        return aes.encryptBase64(content);
    }
}
