package com.foco.db.util;
/**
 * @author lucoo
 * @version 1.0.0
 * @description 线程级别替换租户字段
 * @date 2021/12/27 14:46
 * @since foco2.3.0
 *
 */
public class TenantContext {
    static ThreadLocal<TenantInfo> tenantInfoThreadLocal=new ThreadLocal<>();
    public static void setTenantInfo(TenantInfo tenantInfo){
        tenantInfoThreadLocal.set(tenantInfo);
    }
    public static TenantInfo getTenantInfo(){
        return tenantInfoThreadLocal.get();
    }
    public static void remove(){
        tenantInfoThreadLocal.remove();
    }
}
