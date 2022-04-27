package com.foco.internation.properties;

import com.foco.model.constant.FocoConstants;
import com.foco.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/13 10:36
 */
@ConfigurationProperties(prefix = InternationalProperties.PREFIX)
@Getter
@Setter
public class InternationalProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"international";
    private boolean enabled=true;
    private String appId;
    private Type type;
    /**
     * naocs的group
     */
    private String group;
    /**
     * 状态码的dataId
     */
    private List<String> statusDataId=new ArrayList<>();
    /**
     * 错误码的dataId
     */
    private List<String> errorDataId=new ArrayList<>();
    public static InternationalProperties getConfig(){
        return getConfig(InternationalProperties.class);
    }
    enum Type{
        local,nacos
    }
}
