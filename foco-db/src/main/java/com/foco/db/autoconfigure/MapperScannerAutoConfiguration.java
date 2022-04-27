package com.foco.db.autoconfigure;

import cn.hutool.core.util.StrUtil;
import com.foco.model.constant.FocoConstants;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * description: mybatis basePackage 设置
 *
 * @Author lucoo
 * @Date 2021/6/26 14:13
 */
@Slf4j
public class MapperScannerAutoConfiguration implements EnvironmentAware {
    private String mapperPackage;
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(ObjectProvider<CustomMapperScannerConfigurer> configurer){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //设置sqlSession工厂
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //设置mapper接口扫码包
        String basePackage= FocoConstants.MYBATIS_BASE_PACKAGE;
        if(StrUtil.isNotBlank(mapperPackage)){
            //设置默认值
            basePackage += ","+mapperPackage;
        }
        if(configurer.getIfAvailable()!=null&&StrUtil.isNotBlank(configurer.getIfAvailable().getBasePackage())){
                basePackage += ","+configurer.getIfAvailable().getBasePackage();
        }
        log.info("mybatis basePackage:{}",basePackage);
        mapperScannerConfigurer.setBasePackage(basePackage);
        return mapperScannerConfigurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("mybatis-plus.base-package");
        //String placeholders = environment.resolvePlaceholders("${mybatis-plus.base-package}");
        if(StrUtil.isEmpty(property)){
            property=null;
        }
        mapperPackage=property;
    }
}
