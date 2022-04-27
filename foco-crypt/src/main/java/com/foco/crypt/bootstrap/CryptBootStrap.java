package com.foco.crypt.bootstrap;

import com.foco.context.util.BootStrapPrinter;
import com.foco.crypt.autoconfigure.CryptConfiguration;
import com.foco.crypt.autoconfigure.CryptGatewayAutoConfiguration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/14 18:14
 */
@Import({CryptConfiguration.class, CryptGatewayAutoConfiguration.class})
public class CryptBootStrap {
    @PostConstruct
    public void init(){
        BootStrapPrinter.log("foco-crypt",this.getClass());
    }
}
