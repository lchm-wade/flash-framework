package com.foco.db.transmit;
import com.foco.context.executor.ThreadLocalTransmit;
import com.foco.db.util.TenantContext;
import com.foco.db.util.TenantInfo;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/27 14:53
 * @since foco2.1.0
 */
public class TenantTransmit implements ThreadLocalTransmit<TenantInfo> {
    @Override
    public void set(TenantInfo tenantInfo) {
        TenantContext.setTenantInfo(tenantInfo);
    }
    @Override
    public TenantInfo get() {
        return TenantContext.getTenantInfo();
    }

    @Override
    public void remove() {
        TenantContext.remove();
    }
}
