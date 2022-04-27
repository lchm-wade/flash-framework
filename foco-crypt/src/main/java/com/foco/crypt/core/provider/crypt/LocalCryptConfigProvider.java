package com.foco.crypt.core.provider.crypt;

import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.CryptRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class LocalCryptConfigProvider implements CryptConfigProvider {
    @Autowired
    private CryptProperties cryptProperties;
    @Override
    public CryptProperties getCryptProperties(CryptRequest cryptRequest) {
        return cryptProperties;
    }
}
