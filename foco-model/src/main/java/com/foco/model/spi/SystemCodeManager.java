package com.foco.model.spi;

import java.util.ServiceLoader;

public class SystemCodeManager {
    private static SystemCodeProvider systemCodeProvider;
    private static final String DEFAULT_SUCCESS_CODE="200";
    private static final String DEFAULT_SUCCESS_CODE_PREFIX="SYS";
    static {
        if(systemCodeProvider==null){
            synchronized (SystemCodeManager.class){
                if(systemCodeProvider==null){
                    ServiceLoader<SystemCodeProvider> successCodeProviders = ServiceLoader.load(SystemCodeProvider.class);
                    for(SystemCodeProvider api:successCodeProviders){
                        systemCodeProvider =api;
                        break;
                    }
                }
            }
        }
    }
    public static String getSuccessCode(){
        if(systemCodeProvider ==null){
            return DEFAULT_SUCCESS_CODE;
        }
        return systemCodeProvider.getSuccessCode();
    }
    public static String getSystemCodePrefix(){
        if(systemCodeProvider ==null){
            return DEFAULT_SUCCESS_CODE_PREFIX;
        }
        return systemCodeProvider.getSystemCodePrefix();
    }
}
