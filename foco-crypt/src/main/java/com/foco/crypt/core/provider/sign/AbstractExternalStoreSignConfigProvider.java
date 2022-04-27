package com.foco.crypt.core.provider.sign;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.SignRequest;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/16 14:29
 **/
public abstract class AbstractExternalStoreSignConfigProvider implements SignConfigProvider {
    @Autowired
    private SignProperties defaultSignProperties;
    protected abstract SignProperties loadSignConfig(SignProperties signProperties, SignRequest signRequest);
    @Override
    public SignProperties getSignProperties(SignRequest signRequest){
        if(StrUtil.isNotBlank(signRequest.getAppId())){
            return loadSignConfig(defaultSignProperties,signRequest);
        }
        return defaultSignProperties;
    }
}
