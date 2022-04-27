package com.foco.internation.parser.naocs;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.foco.internation.LocaleEntity;
import com.foco.internation.parser.PropertiesParser;
import com.foco.internation.properties.InternationalProperties;
import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/13 10:49
 */
@Slf4j
public class NacosPropertiesParser implements PropertiesParser, InitializingBean {
    @Autowired
    private NacosConfigManager nacosConfigManager;
    @Autowired
    private InternationalProperties internationalProperties;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;
    private static final String spilt="-";
    private String appId;
    @Override
    public String getProperty(LocaleEntity localeEntity, String key) {
        Properties properties = NacosConfigCache.getConfig(appId+localeEntity.getType() + localeEntity.getLocale() + ".properties");
        if(properties!=null){
            return properties.getProperty(key);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        appId=buildAppId();
        String group = StrUtil.isBlank(internationalProperties.getGroup()) ? nacosConfigProperties.getGroup() : internationalProperties.getGroup();
        List<String> errorDataIds = internationalProperties.getErrorDataId();
        ConfigService configService = nacosConfigManager.getConfigService();
        for (String errorDataId : errorDataIds) {
            String config = configService.getConfigAndSignListener(appId+errorDataId, group, 3000L, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String config) {
                    Properties properties = load(config,errorDataId);
                    NacosConfigCache.put(appId+FocoConstants.ERROR_CODE_FILE + errorDataId, properties);
                }
            });
            Properties properties = load(config,errorDataId);
            NacosConfigCache.put(appId+FocoConstants.ERROR_CODE_FILE + errorDataId, properties);
        }

        List<String> statusDataIds = internationalProperties.getStatusDataId();
        for (String statusDataId : statusDataIds) {
            String config = configService.getConfigAndSignListener(appId+statusDataId, group, 3000L, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String config) {
                    Properties properties = load(config,statusDataId);
                    NacosConfigCache.put(appId+FocoConstants.STATUS_CODE_FILE + statusDataId, properties);
                    Properties revert = revert(properties);
                    NacosConfigCache.put(appId+FocoConstants.STATUS_CODE_REVERS_FILE + statusDataId, revert);
                }
            });
            Properties properties = load(config,statusDataId);
            NacosConfigCache.put(appId+FocoConstants.STATUS_CODE_FILE + statusDataId, properties);

            Properties revert = revert(properties);
            NacosConfigCache.put(appId+FocoConstants.STATUS_CODE_REVERS_FILE + statusDataId, revert);
        }
    }
    private Properties revert(Properties properties){
        //翻转properties
        Enumeration<?> enumeration = properties.propertyNames();
        Properties propertiesRevers = new Properties();
        while (enumeration.hasMoreElements()){
            Object key = enumeration.nextElement();
            String value = properties.getProperty(String.valueOf(key));
            propertiesRevers.put(value,key);
        }
        return propertiesRevers;
    }
    private Properties load(String propertiesString,String statusDataId) {
        if(StrUtil.isBlank(propertiesString)){
            SystemException.throwException(FocoErrorCode.PATH_ERROR.getCode(),String.format("dataId:%s不存在",statusDataId));
        }
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(propertiesString));
        } catch (IOException e) {
            log.error("加载配置文件异常", e);
        }
        return properties;
    }
    private String buildAppId(){
        if(StrUtil.isNotBlank(internationalProperties.getAppId())){
            return internationalProperties.getAppId()+spilt;
        }
        return "";
    }
}
