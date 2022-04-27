package com.foco.crypt.core.algorithm.crypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.algorithm.Crypt;

/**
 * @Description des 加解密
 * @Author lucoo
 * @Date 2021/6/15 14:21
 **/
@Crypt(algorithm=CryptProperties.Algorithm.DES)
public class DesCryptAlgorithm implements CryptAlgorithm {
    @Override
    public String deCrypt(CryptProperties cryptProperties,String content){
        DES des = SecureUtil.des(cryptProperties.getCryptKey().getBytes());
        return des.decryptStr(content);
    }

    @Override
    public String enCrypt(CryptProperties cryptProperties,String content){
        DES des = SecureUtil.des(cryptProperties.getCryptKey().getBytes());
        return des.encryptBase64(content);
    }
}
