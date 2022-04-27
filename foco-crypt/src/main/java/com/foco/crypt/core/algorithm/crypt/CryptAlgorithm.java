package com.foco.crypt.core.algorithm.crypt;

import com.foco.crypt.properties.CryptProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:21
 **/
public interface CryptAlgorithm {
    /**
     * 解密
     * @return 明文
     */
    String deCrypt(CryptProperties cryptProperties, String content);

    /**
     * 加密
     * @param content 待加密json串
     * @return 加密的密文
     */
    String enCrypt(CryptProperties cryptProperties, String content);
}
