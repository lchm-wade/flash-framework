package com.foco.sharding.jdbc;

import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.MainClassConstant;
import com.foco.model.spi.ExcludeAutoConfigure;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description 排除druid的自动装配
 * @Author lucoo
 * @Date 2021/7/13 15:07
 **/
@Slf4j
public class ExcludeDruidConfigure implements ExcludeAutoConfigure {
    @Override
    public  void exclude(List<String> excludeList,Object environment) {
        excludeList.add(MainClassConstant.DRUID_DATA_SOURCE_AUTO_CONFIGURATION_CLASS);
    }
}
