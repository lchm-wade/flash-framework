package com.foco.crypt.core.provider.sign;

import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.SignRequest;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:07
 **/
public interface SignConfigProvider {
    SignProperties getSignProperties(SignRequest signRequest);
}
