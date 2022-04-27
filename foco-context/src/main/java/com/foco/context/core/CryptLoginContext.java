package com.foco.context.core;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 *
 * @author lucoo
 * @date 2021/1/28 14:49
 */
@Setter
@Getter
public class CryptLoginContext extends GenericLoginContext{
    /**
     * 当前用户加解密key
     **/
    private String cryptKey;
    /**
     * 非对称 当前用户加解密key 公钥
     **/
    private String cryptPartyPublicKey;
    /**
     * 非对称 当前用户加解密key(平台对应用户)私钥
     **/
    private String cryptPlatformPrivateKey;
    /**
     * 当前用户加解签key
     **/
    private String signKey;
    /**
     * 非对称 当前用户加解签 公钥key
     **/
    private String signPartyPublicKey;
    /**
     * 非对称 当前用户加解签key(平台对应用户)私钥
     **/
    private String signPlatformPrivateKey;
}
