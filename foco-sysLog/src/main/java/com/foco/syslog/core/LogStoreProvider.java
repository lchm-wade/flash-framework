package com.foco.syslog.core;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/22 19:03
 **/
public interface LogStoreProvider {
    void save(LogParam logParam);
}
