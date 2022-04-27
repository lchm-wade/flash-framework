package com.foco.db.transmit;
import com.foco.context.executor.ThreadLocalTransmit;
import com.foco.db.util.IgnoreHelper;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/27 14:53
 * @since foco2.1.0
 */
public class IgnoreTenantTransmit implements ThreadLocalTransmit<Boolean> {
    @Override
    public void set(Boolean ignore) {
        IgnoreHelper.setTenantFlag(ignore);
    }
    @Override
    public Boolean get() {
        return IgnoreHelper.getTenant();
    }

    @Override
    public void remove() {
        IgnoreHelper.removeTenant();
    }
}
