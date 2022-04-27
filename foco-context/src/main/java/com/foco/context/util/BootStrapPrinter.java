package com.foco.context.util;

import com.foco.context.logging.FocoVersion;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/28 17:28
 * @since foco2.1.0
 */
@Slf4j
public class BootStrapPrinter {
    private static Map<String,String> versionCache=new HashMap<>();
    static String info=" is ready inject";

    public static Map<String, String> getVersionCache() {
        return versionCache;
    }

    /**
     * @since foco2.3.0
     */
    public static void log(String name,Class mainClass){
        String version = FocoVersion.getVersion(mainClass);
        versionCache.put(name,version);
        log.info(name+":"+version +info);
    }

    /**
     * 兼容2.3.0之前的版本
     * @param name
     */
    public static void log(String name){
        log.info(name+":"+FocoVersion.getVersion(BootStrapPrinter.class) +info);
    }
}
