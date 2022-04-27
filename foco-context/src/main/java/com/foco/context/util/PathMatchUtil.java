package com.foco.context.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;

/**
 * @Description 路径匹配
 * /ff/getUser 匹配 /ff/**
 * @Author lucoo
 * @Date 2021/5/13 17:18
 */

public class PathMatchUtil {
    private static PathMatcher pathMatcher= new AntPathMatcher();
    public static boolean match(List<String> patterns, String path){
        for(String pattern:patterns){
            boolean match = pathMatcher.match(pattern, path);
            if(match){
                return true;
            }
        }
        return false;
    }
}
