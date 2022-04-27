package com.foco.auth.common;

import com.foco.auth.properties.TokenProperties;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 16:38
 **/
@Slf4j
public abstract class AbstractLoginAuth implements LoginAuth, InitializingBean {
    @Autowired
    protected GenericTokenManager tokenManager;
    @Autowired
    protected TokenProperties tokenProperties;
    Class<? extends GenericLoginContext> loginContextClazz = GenericLoginContext.class;
    public void afterPropertiesSet() {
        if(tokenProperties.getTokenClass()!=null){
            if(GenericLoginContext.class.isAssignableFrom(loginContextClazz)){
                loginContextClazz = tokenProperties.getTokenClass();
            }else {
                log.warn("配置的类{}不是GenericLoginContext的子类,忽略子类化", tokenProperties.getTokenClass());
            }
        }
    }
    protected GenericLoginContext doAuth(String tokenKey) {
        GenericLoginContext loginContext = tokenManager.parseToken(tokenKey, loginContextClazz);
        return loginContext;
    }
}
