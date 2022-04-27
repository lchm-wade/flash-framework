package com.foco.crypt.autoconfigure;

import com.foco.crypt.inteceptor.gateway.RequestBodyDeCryptFilter;
import com.foco.crypt.inteceptor.gateway.ResponseBodyEnCryptFilter;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/14 18:12
 */
@ConditionalOnClass(name = MainClassConstant.SPRING_CLOUD_GATEWAY)
public class CryptGatewayAutoConfiguration {
    @Bean
    RequestBodyDeCryptFilter requestBodyDeCryptFilter(){
        return new RequestBodyDeCryptFilter();
    }
    @Bean
    ResponseBodyEnCryptFilter responseBodyEnCryptFilter(){
        return new ResponseBodyEnCryptFilter();
    }
}
