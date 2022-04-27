package com.foco.file.properties;

import com.foco.properties.AbstractProperties;
import com.foco.model.constant.FocoConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 9:52
 **/
@Getter
@Setter
@ConfigurationProperties(prefix=FileProperties.PREFIX)
public class FileProperties extends AbstractProperties {
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"file";
    public static FileProperties getConfig(){
        return getConfig(FileProperties.class);
    }
     /**
      * 文件存储方式 默认local
      * local:本地服务器存储
      * cloud:云存储,暂不支持,后期对接阿里云OSS或者腾讯云COS
     **/
    private FileStoreEnum store= FileStoreEnum.local;
     /**
      * 本地存储时,若应用集群部署需要指定节点id
      * 需要指定当前机器的节点,
      * 用来做文件分布式存储
     **/
    private Integer nodeId=1;
    /**
     * 本地存储时,存在服务器上的路径
     **/
    private String storePath;
     /**
      * 文件最大大小 以字节为单位,默认5M
     **/
    private Long maxSize=5*1024*1024L;
    private String accessUrl="http://";
    public enum FileStoreEnum{
        local,cloud
    }
}
