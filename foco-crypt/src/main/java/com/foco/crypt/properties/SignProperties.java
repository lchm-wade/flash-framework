package com.foco.crypt.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 加解密
 * @Author lucoo
 * @Date 2021/6/2 14:43
 **/
@ConfigurationProperties(prefix = SignProperties.PREFIX)
@Getter
@Setter
public class SignProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"sign";
    public static SignProperties getConfig(){
        return getConfig(SignProperties.class);
    }

    /**
     *签名
     */
    private String signHead="sign";
    /**
     * 是否开启 加解签 默认false
     */
    private Boolean enabled=false;
    /**
     * 加签算法 请求头
     */
    private String signAlgorithmHead="signAlgorithm";
    /**
     * 加解签算法 默认MD5
     * 如请求中带有加解签算法,则此配置失效
     */
    private Algorithm algorithm= Algorithm.MD5;


    /**
     * 加解签的URL
     */
    private List<String> signUrls=new ArrayList<>();
    /**
     * 特殊url,该配置用来指定url是input,output,all
     * 配置形式 /xx/yy:input
     **/
    private List<String> signModelUrls=new ArrayList<>();
    /**
     * 默认 EXCLUDE
     * cryptList 配置的URL 是参与或者不参与加解密
     * INCLUDE: cryptList配置的URL 进行加解密
     * EXCLUDE cryptList配置的URL 不进行加解密
     */
    private FilterType filterType= FilterType.EXCLUDE;
    /**
     * 报文是否需要解密
     * ALL:请求响应都需要加解密
     * INPUT:请求进来需要解密
     * OUTPUT:响应回去需要加密
     */
    private  SignModel signModel= SignModel.ALL;
    /**
     * 加签的密钥
     */
    private String signKey="dsf3259s@F8EU$8789IFDHG45U)POIJHIU98";
    /**
     * RSA 模式下
     * 采用自己私钥加密,对端公钥解密
     * 对端的公钥 用于解密对端的数据
     */
    private String signPartyPublicKey;
    /**
     * RSA 模式下 平台私钥
     */
    private String signPlatformPrivateKey;
    /**
     * 秘钥是否外部存储,默认LOCAL
     * 当取值redis或mysql时,是为了对接多端外部系统,则本地配置的私钥失效
     * 会根据请求头的渠道id去获取对应的私钥,若获取不到则用本地配置的
     * REDIS:存储于redis
     * DB：存储于mysql
     * LOCAL: 存储本地配置文件
     */
    private StoreEnum store=StoreEnum.LOCAL;
    /**
     * 防重放攻击时允许客户端和服务端的时间差(毫秒),默认1分钟
     */
    private long ignoreMillis=60000L;
    /**
     * config为redis时，存储的key前缀
     * 默认crypt:appId:xxx
     * xxx为appId具体值,故存储配置时应该以该key来存储
     * 存储的对象
     */
    private String redisPrefix="sign:appId:";
    public enum Algorithm{
        MD5,CUSTOM;
    }
    public enum FilterType{
        INCLUDE, EXCLUDE;
    }
    public enum SignModel{
        ALL, INPUT,OUTPUT;
    }
}
