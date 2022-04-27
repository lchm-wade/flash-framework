package com.foco.crypt.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:08
 **/
@Getter
@Setter
public class CryptRequest {
    /**
     * 加解密算法
     */
    private String algorithm;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 请求url
     */
    private String url;
    /**
     * 密文
     */
    private String content;
}
