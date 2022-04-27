package com.foco.crypt.core.algorithm.crypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.algorithm.Crypt;

/**
 * @Description Rsa 加解密
 * @Author lucoo
 * @Date 2021/6/15 14:21
 **/
@Crypt(algorithm=CryptProperties.Algorithm.RSA)
public class RsaCryptAlgorithm implements CryptAlgorithm {
    @Override
    public String deCrypt(CryptProperties cryptProperties,String content){
        RSA rsa = SecureUtil.rsa(cryptProperties.getCryptPlatformPrivateKey(),cryptProperties.getCryptPartyPublicKey());
        return rsa.decryptStr(content, KeyType.PublicKey);
    }
    @Override
    public String enCrypt(CryptProperties cryptProperties,String content){
        RSA rsa = SecureUtil.rsa(cryptProperties.getCryptPlatformPrivateKey(),cryptProperties.getCryptPartyPublicKey());
        return rsa.encryptBase64(content,KeyType.PrivateKey);
    }
}
