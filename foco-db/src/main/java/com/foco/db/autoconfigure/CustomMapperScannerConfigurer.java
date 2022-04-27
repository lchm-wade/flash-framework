package com.foco.db.autoconfigure;

/**
 * <p> 扩展MapperScannerConfigurer，提供修改mybatis mapper 包扫描参数 BasePackage 配置 </p>
 *
 * @Author lucoo
 * @Date 2021/6/26 14:13
 */
public interface CustomMapperScannerConfigurer {

    /**
     * 初始化mybatis 扫描 mapper 包路径
     * @return basePackage
     */
    String getBasePackage();
}
