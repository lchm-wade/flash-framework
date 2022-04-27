package com.foco.internation.parser.naocs;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/13 11:53
 */
public class NacosConfigCache {
    private static Map<String, Properties> config=new HashMap<>();
    public static void put(String key,Properties properties){
        config.put(key,properties);
    }
    public static Properties getConfig(String key){
        return config.get(key);
    }

}
