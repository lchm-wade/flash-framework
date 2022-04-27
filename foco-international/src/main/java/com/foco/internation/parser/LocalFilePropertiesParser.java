package com.foco.internation.parser;

import com.foco.internation.LocaleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * @Description todo
 * @Author lucoo
 * @Date 2021/5/12 19:50
 */
public class LocalFilePropertiesParser implements PropertiesParser {
    @Autowired
    private ConfigurableEnvironment environment;
    @Override
    public String getProperty(LocaleEntity localeEntity, String key) {
        PropertiesPropertySource propertySource = (PropertiesPropertySource) environment.getPropertySources().get(localeEntity.getType()+localeEntity.getLocale()+".properties");
        if(propertySource==null||propertySource.getProperty(key)==null){
            return null;
        }
        return String.valueOf(propertySource.getProperty(key));
    }
}
