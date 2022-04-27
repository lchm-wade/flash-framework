package com.foco.crypt.core.provider.sign;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.SignProperties;
import com.foco.context.core.CryptLoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.SignRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class DefaultSignConfigProvider implements SignConfigProvider {
    @Autowired
    private SignProperties signProperties;
    @Override
    public SignProperties getSignProperties(SignRequest signRequest) {
        SignProperties properties = new SignProperties();
        BeanCopierEx.copyProperties(signProperties, properties);
        CryptLoginContext loginContext = LoginContextHolder.getLoginContext(CryptLoginContext.class);
        if(loginContext!=null){
            if(StrUtil.isNotBlank(loginContext.getSignKey())){
                properties.setSignKey(loginContext.getSignKey());
            }
            if(StrUtil.isNotBlank(loginContext.getSignPartyPublicKey())){
                properties.setSignPartyPublicKey(loginContext.getSignPartyPublicKey());
            }if(StrUtil.isNotBlank(loginContext.getSignPlatformPrivateKey())){
                properties.setSignPlatformPrivateKey(loginContext.getSignPlatformPrivateKey());
            }
        }
        return properties;
    }
}