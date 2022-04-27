package com.foco.swagger.autoconfigure;
import com.foco.model.constant.MainClassConstant;
import com.foco.properties.SystemConfig;
import com.foco.swagger.header.GlobalOperationParameter;
import com.foco.swagger.header.SwaggerHeaderManager;
import com.foco.swagger.header.SwaggerHeaderParameter;
import com.foco.swagger.properties.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/23 18:16
 **/
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnMissingClass(MainClassConstant.SPRING_CLOUD_GATEWAY)
@Slf4j
public class SwaggerAutoConfiguration{
    @Autowired
    private SwaggerProperties swaggerProperties;
    @Bean
    SwaggerHeaderManager swaggerHeaderManager(SystemConfig systemConfig, ObjectProvider<SwaggerHeaderParameter> swaggerHeaderParameters){
        return new SwaggerHeaderManager(systemConfig,swaggerHeaderParameters);
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerHeaderManager swaggerHeaderManager) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getGlobalParameters(swaggerHeaderManager));
    }
    private List<Parameter> getGlobalParameters(SwaggerHeaderManager swaggerHeaderManager) {
        List<Parameter> globalParamList = new ArrayList<>();
        List<GlobalOperationParameter> list = swaggerHeaderManager.getGlobalOperationParameters();
        if (list != null) {
            ParameterBuilder parameterBuilder = new ParameterBuilder();
            list.forEach(l -> {
                Parameter tokenParam = parameterBuilder.name(l.getName()).description(l.getDescription())
                        .modelRef(new ModelRef(l.getModelRef())).parameterType(l.getParameterType())
                        .required(l.getRequired()).build();
                globalParamList.add(tokenParam);
            });
        }
        return globalParamList;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description("接口在线文档")
                .version("1.0")
                .build();
    }
}
