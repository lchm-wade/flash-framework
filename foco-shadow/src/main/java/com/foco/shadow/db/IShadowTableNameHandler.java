package com.foco.shadow.db;

import org.apache.ibatis.reflection.MetaObject;
/**
 * @author zachary
 * @version 1.0.0
 * @description 影子表表名处理器
 * @date 2021/11/1
 */
public interface IShadowTableNameHandler {
    /**
     * 生成影子表表名
     *
     * @param metaObject 元对象
     * @param sql        当前执行 SQL
     * @param tableName  表名
     * @return String
     */
    String genShadowTableName(MetaObject metaObject, String sql, String tableName);
}
