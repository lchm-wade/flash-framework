package com.foco.swagger.header;

import com.foco.properties.SystemConfig;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/11 14:41
 */
public class SwaggerHeaderManager {
    List<GlobalOperationParameter> globalOperationParameters;
    public  SwaggerHeaderManager(SystemConfig systemConfig, ObjectProvider<SwaggerHeaderParameter> swaggerHeaderParameters){
        globalOperationParameters=new ArrayList<>();
        GlobalOperationParameter authParam=new GlobalOperationParameter();
        authParam.setName(systemConfig.getTokenHead());
        authParam.setParameterType("header");
        authParam.setRequired(false);
        authParam.setModelRef("String");
        authParam.setDescription("token");
        globalOperationParameters.add(authParam);

        GlobalOperationParameter languageParam=new GlobalOperationParameter();
        languageParam.setName(systemConfig.getLocaleHead());
        languageParam.setParameterType("header");
        languageParam.setRequired(false);
        languageParam.setModelRef("String");
        languageParam.setDescription("语言类型");
        globalOperationParameters.add(languageParam);
        if(swaggerHeaderParameters.getIfAvailable()!=null){
            swaggerHeaderParameters.getIfAvailable().add(globalOperationParameters);
        }
    }

    public List<GlobalOperationParameter> getGlobalOperationParameters() {
        return globalOperationParameters;
    }
}
