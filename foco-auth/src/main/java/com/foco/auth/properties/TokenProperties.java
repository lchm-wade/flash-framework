package com.foco.auth.properties;

import com.foco.properties.AbstractProperties;
import com.foco.context.core.GenericLoginContext;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/2 14:43
 **/
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
@Getter
@Setter
public class TokenProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"token";
    public static TokenProperties getConfig(){
        return getConfig(TokenProperties.class);
    }
    private boolean enabled=true;
    /**
     * 32位的token生成的加密key
     */
    private String secretKey="dfrtgyujsefqwersfth56782hy731qad";
    /**
     * 登录白名单,不做登录态校验
     */
    private List<String> urlList=new ArrayList<>();
    /**
     * 是否白名单,默认true,黑名单模式可以配置成false
     */
    private Boolean isWhite=true;
    /**
     * 客户端类型 请求头
     */
    private String clientTypeHead="clientType";
    /**
     * 网络内型 请求头
     */
    private String networkTypeHead="networkType";
    /**
     * 是否每次访问都刷新token有效期,默认false
     */
    private boolean refreshTokenPerAccess=false;
    /**
     * token 类型 可取值 redis,db,jwt
     */
    private TokenType tokenType;
    /**
     * GenericLoginContext的子类
     */
    private Class<? extends GenericLoginContext> tokenClass;
    enum TokenType{
        redis, db, jwt;
    }
}
