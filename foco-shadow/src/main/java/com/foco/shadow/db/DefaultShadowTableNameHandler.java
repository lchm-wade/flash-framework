package com.foco.shadow.db;

import com.foco.shadow.properties.ShadowProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Set;

/**
 * @author zachary
 * @version 1.0.0
 * @description TODO
 * @date 2021/11/1
 */
public class DefaultShadowTableNameHandler implements IShadowTableNameHandler {
    private ShadowProperties shadowProperties;
    private static final String LINK_SYMBOL="_";

    public DefaultShadowTableNameHandler(ShadowProperties shadowProperties) {
        this.shadowProperties = shadowProperties;
    }

    @Override
    public String genShadowTableName(MetaObject metaObject, String sql, String tableName) {
        Set<String> ignoreShadowTableNames=shadowProperties.getIgnoreShadowTableNames();
        if(ignoreShadowTableNames!=null&&ignoreShadowTableNames.contains(tableName)){
            return tableName;
        }
        return tableName+LINK_SYMBOL+shadowProperties.getFlag();
    }
}
