package com.foco.crypt.core.provider.crypt;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.model.constant.MainClassConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/16 14:29
 **/
public abstract class AbstractExternalStoreCryptConfigProvider implements CryptConfigProvider {
    @Autowired
    private CryptProperties defaultCryptProperties;
    protected abstract CryptProperties loadCryptConfig(CryptProperties cryptProperties, CryptRequest cryptRequest);
    @Override
    public CryptProperties getCryptProperties(CryptRequest cryptRequest) {
        if(StrUtil.isNotBlank(cryptRequest.getAppId())){
            return loadCryptConfig(defaultCryptProperties,cryptRequest);
        }
        return defaultCryptProperties;
    }

}
