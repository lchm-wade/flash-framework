package com.foco.crypt.core.algorithm.sign;

import cn.hutool.crypto.SecureUtil;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.algorithm.Sign;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/1/6 15:10
 **/
@Sign(algorithm= SignProperties.Algorithm.MD5)
@Slf4j
public class Md5SignAlgorithm extends AbstractSignAlgorithm {
    @Override
    public boolean doCheckSign(String sign, String content,SignProperties signProperties) {
        String deCodeSign = SecureUtil.md5(content + signProperties.getSignKey());
        boolean equals = deCodeSign.equals(sign);
        if(!equals){
            log.warn("签名后的值:{},签名原值:{}",deCodeSign,sign);
        }
        return equals;
    }

    @Override
    public String doSign(String content, SignProperties signProperties) {
        return SecureUtil.md5(content+signProperties.getSignKey());
    }
}
