package com.foco.auth.bootstrap;

import com.foco.auth.autoconfigure.AuthAutoConfiguration;
import com.foco.auth.autoconfigure.AuthFilterAutoConfiguration;
import com.foco.auth.autoconfigure.AuthInterceptorConfiguration;
import com.foco.context.util.BootStrapPrinter;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 9:46
 **/
@Import({AuthAutoConfiguration.class, AuthFilterAutoConfiguration.class, AuthInterceptorConfiguration.class})
public class AuthBootStrap {
    @PostConstruct
    public void init(){
        BootStrapPrinter.log("foco-auth",this.getClass());
    }
}
