package com.foco.http.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.http.annotation.HttpScan;
import com.foco.http.properties.HttpProperties;
import com.foco.http.interceptor.CryptInterceptor;
import com.foco.model.constant.MainClassConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/10 16:15
 **/
@HttpScan({"${foco.http.base-package}"})
@Import(ConverterFactoryConfiguration.class)
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {
    @PostConstruct
    public void init(){
        BootStrapPrinter.log("foco-http",this.getClass());
    }
    @Bean
    @ConditionalOnClass(name={MainClassConstant.FOCO_CRYPT})
    CryptInterceptor cryptInterceptor(){
        return new CryptInterceptor();
    }
}
