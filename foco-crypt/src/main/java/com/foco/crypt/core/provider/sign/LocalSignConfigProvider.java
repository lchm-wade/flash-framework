package com.foco.crypt.core.provider.sign;

import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.SignRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class LocalSignConfigProvider implements SignConfigProvider {
    @Autowired
    private SignProperties signProperties;
    @Override
    public SignProperties getSignProperties(SignRequest signRequest) {
        return signProperties;
    }
}
