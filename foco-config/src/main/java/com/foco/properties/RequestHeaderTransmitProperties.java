package com.foco.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/11/20 14:15
 */
@Getter
@Setter
@ConfigurationProperties(prefix= RequestHeaderTransmitProperties.PREFIX)
public class RequestHeaderTransmitProperties extends AbstractProperties{
    public static RequestHeaderTransmitProperties getConfig(){
        return getConfig(RequestHeaderTransmitProperties.class);
    }
    public static final String PREFIX="foco.transmit";
    /**
     * 配置在这里的请求头就会做透传
     */
    private List<String> headers=new ArrayList<>();
}
