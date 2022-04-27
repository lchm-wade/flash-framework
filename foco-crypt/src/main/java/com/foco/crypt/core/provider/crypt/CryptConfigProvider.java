package com.foco.crypt.core.provider.crypt;

import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.CryptRequest;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:07
 **/
public interface CryptConfigProvider {
    CryptProperties getCryptProperties(CryptRequest cryptRequest);
}
