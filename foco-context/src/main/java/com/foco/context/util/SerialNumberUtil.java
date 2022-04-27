package com.foco.context.util;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/30 18:22
 */
public class SerialNumberUtil {
    public static String splice(long result,int totalLen){
        int len=String.valueOf(result).length();
        if(len>=totalLen){
            return String.valueOf(result);
        }
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<totalLen-len;i++){
            builder.append("0");
        }
        builder.append(result);
        return builder.toString();
    }
}
