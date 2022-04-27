package com.foco.syslog.provider.db;

import com.foco.context.util.BeanCopierEx;
import com.foco.syslog.core.LogParam;
import com.foco.syslog.core.LogStoreProvider;
import com.foco.syslog.provider.db.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/22 19:21
 **/
public class DbLogStore implements LogStoreProvider {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Override
    public void save(LogParam logParam) {
        SysLog sysLog = BeanCopierEx.copyProperties(logParam, SysLog.class);
        sysLogMapper.insert(sysLog);
    }
}
