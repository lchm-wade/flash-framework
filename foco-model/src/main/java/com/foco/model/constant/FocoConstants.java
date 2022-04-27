package com.foco.model.constant;

/**
 * description: 系统常量配置
 *
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
public interface FocoConstants {
    String CURRENT_ENV = "spring.profiles.active";
    /***主包名定义*/
    String MAIN_PACKAGE_CLOUD = "com.foco.cloud.core.feign";
    String MAIN_PACKAGE_WEB = "com.foco.boot.web.interceptor";
    String MAIN_PACKAGE_SWAGGER = "com.foco.swagger.gateway";
    String MAIN_PACKAGE_CRYPT = "com.foco.crypt";

    String MYBATIS_BASE_PACKAGE = "com.foco.**.mapper.**";
    String MYBATIS_ENTITY_BASE_PACKAGE = "classpath*:com/foco/**/entity/*.class";
    String API_RESPONSE_TRACE_KEY = "X-B3-TraceId";
    String API_RESPONSE_SPAN_KEY = "X-B3-SpanId";
    String API_RESPONSE_PARENT_SPAN_KEY = "X-B3-ParentSpanId";


    String PARENT_SPAN_ID = "parent-span-id";
    String PARENT_TRACE_ID = "parent-trace-id";
    int HIGHEST_PRECEDENCE = -2147483648;

    String ERROR_CODE_FILE = "error-";
    String STATUS_CODE_FILE = "status-";
    String STATUS_CODE_REVERS_FILE = "status-revers-";

    String FEIGN_ORIGINAL = "feign";
    String ORIGINAL = "foco.request.original";

    String PAGE = "page";
    String PAGE_CURRENT = "current";
    String PAGE_SIZE = "size";

    String ENABLED = "enabled";
    String CONFIG_PREFIX = "foco.";

    /**
     * 默认route
     */
    String DEFAULT_ROUTE = "default";

    /**
     * 3.x删除
     */
    @Deprecated
    String CROSS_GROUP_CALL = CONFIG_PREFIX + "feign.group";
    /**
     * 3.x删除
     */
    @Deprecated
    String CROSS_REGISTER_ID_CALL = CONFIG_PREFIX + "feign.registerId";
    String CROSS_TENANT_NAME_CALL = CONFIG_PREFIX + "feign.tenant.name";
    String CROSS_SERVICE_NAME_CALL = CONFIG_PREFIX + "feign.service.name";
    String HTTP_CONTEXT = CONFIG_PREFIX + "http.context";
    String FOCO_CONTEXT="foco-context";
    String SKY_WALKING_LOG_TAG=CONFIG_PREFIX+"input";
    String FOCO_ENV=CONFIG_PREFIX+"env";
}
