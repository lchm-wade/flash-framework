package com.foco.swagger.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-16 15:45
 */
@Slf4j
public class CompositeSwaggerResource implements SwaggerResourcesProvider {
    /**
     * swagger默认的url后缀
     */
    protected static final String API_URI = "v2/api-docs";

    /**
     * 网关路由
     */
    @Resource
    private RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //获取所有路由的ID
        /*List<String> finalRoutes = routes;
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !appName.equals(route.getUri().getHost()))
                .subscribe(route -> finalRoutes.add(route.getUri().getHost()));
        Set<String> dealed = new HashSet<>();
        if (!routeAll) {
            List<String> gateWay = gatewayProperties.getRoutes().stream().map(routeDefinition -> routeDefinition.getUri().getHost()).collect(Collectors.toList());
            routes=routes.stream().filter(instance->gateWay.contains(instance)).collect(Collectors.toList());
        }
        // 记录已经添加过的server，存在同一个应用注册了多个服务在注册中心上
        routes.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + API_URI;
            if (!dealed.contains(url)) {
                dealed.add(url);
                resources.add(swaggerResource(instance,url));
            }
        });*/

        //获取所有路由的ID
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !appName.equals(route.getUri().getHost()))
                .subscribe(route -> routes.add(route.getUri().getHost()));
        //匹配配置文件中配置的路由
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getUri().getHost())).forEach(route -> {
            route.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                    .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                    .replace("**", API_URI))));
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String url) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setUrl(url);
        swaggerResource.setName(name);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
