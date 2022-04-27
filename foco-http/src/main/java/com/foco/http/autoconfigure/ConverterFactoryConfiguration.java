package com.foco.http.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 15:42
 **/
@Configuration
@ConditionalOnClass(name = "retrofit2.converter.simplexml.SimpleXmlConverterFactory")
public class ConverterFactoryConfiguration {
    @Bean
    public SimpleXmlConverterFactory simpleXmlConverterFactory(){
        return SimpleXmlConverterFactory.create();
    }
}
