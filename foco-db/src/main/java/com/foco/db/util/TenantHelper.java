package com.foco.db.util;

import java.util.function.Supplier;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 线程级别替换租户字段
 * @date 2021/12/27 14:46
 * @since foco2.3.0
 *
 */
public class TenantHelper {
    public static <T> T replaceTenant(TenantInfo tenantInfo,Supplier<T> supplier){
        TenantContext.setTenantInfo(tenantInfo);
        T t = supplier.get();
        TenantContext.remove();
        return t;
    }
}
