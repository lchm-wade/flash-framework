package com.foco.limit;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:52
 **/
@Getter
@Setter
@ConfigurationProperties(prefix=LimitProperties.PREFIX)
public class LimitProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"limit";
    public static LimitProperties getConfig(){
        return getConfig(LimitProperties.class);
    }
      /**
       * 配置的项不参与限频
       * 配置形式如下:
       * className:methodName
       * com.xx.xx.UserController:test
      **/
    private List<String> whiteList=new ArrayList<>();
    /**单位时间内
     * 允许访问次数
     * 默认1次*/
    private int limitCount=1;
    /**限流时间
     * 以秒为单位
     * 默认3秒*/
    private long limitTime=3;
}
