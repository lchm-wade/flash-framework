package com.foco.crypt.core.provider.crypt;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.CryptProperties;
import com.foco.context.core.CryptLoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.CryptRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class DefaultCryptConfigProvider implements CryptConfigProvider {
    @Autowired
    private CryptProperties cryptProperties;
    @Override
    public CryptProperties getCryptProperties(CryptRequest cryptRequest) {
        CryptProperties properties=new CryptProperties();
        BeanCopierEx.copyProperties(cryptProperties,properties);
        CryptLoginContext loginContext = LoginContextHolder.getLoginContext(CryptLoginContext.class);
        if(loginContext!=null){
            if(StrUtil.isNotBlank(loginContext.getCryptKey())){
                properties.setCryptKey(loginContext.getCryptKey());
            }
            if(StrUtil.isNotBlank(loginContext.getCryptPartyPublicKey())){
                properties.setCryptPartyPublicKey(loginContext.getCryptPartyPublicKey());
            }
            if(StrUtil.isNotBlank(loginContext.getCryptPlatformPrivateKey())){
                properties.setCryptPlatformPrivateKey(loginContext.getCryptPlatformPrivateKey());
            }
        }
        return properties;
    }
}
