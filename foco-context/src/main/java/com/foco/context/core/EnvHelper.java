package com.foco.context.core;

import cn.hutool.core.util.StrUtil;
import com.foco.model.constant.FocoConstants;
import org.springframework.core.env.Environment;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/09 11:17
 * @since foco2.1.0
 */
public class EnvHelper {
    private static String env;
    static {
        Environment environment = SpringContextHolder.getEnvironment();
        String property;
        property = environment.getProperty(FocoConstants.FOCO_ENV);
        if (StrUtil.isBlank(property)) {
            property = environment.getProperty(FocoConstants.CURRENT_ENV);
        }
        if(StrUtil.isBlank(property)){
            property=Env.LOCAL.getEnv();
        }
        env=property.toLowerCase();
    }

    public static boolean isProd(){
        return Env.PROD.getEnv().equals(env);
    }
    public static boolean isDev(){
        return Env.DEV.getEnv().equals(env);
    }
    public static boolean isTest(){
        return Env.TEST.getEnv().equals(env);
    }
    public static boolean isUat(){
        return Env.UAT.getEnv().equals(env);
    }
    public static boolean isFix(){
        return Env.FIX.getEnv().equals(env);
    }
    public static String getEnv(){
        return env;
    }

}
