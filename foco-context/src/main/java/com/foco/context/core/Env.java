package com.foco.context.core;
/**
 * 支持的环境
 * @Author lucoo
 * @Date 2021/06/15 15:52
 *
 */
public enum Env {
    LOCAL("local"),DEV("dev"), TEST("test"),UAT("uat"),FIX("fix"), PROD("prod");
    String env;

    Env(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }

    /**
     * 是否生产环境
     * @param env
     * @return
     */
    @Deprecated
    public static boolean isProd(Env env) {
        return PROD.equals(env);
    }
}
