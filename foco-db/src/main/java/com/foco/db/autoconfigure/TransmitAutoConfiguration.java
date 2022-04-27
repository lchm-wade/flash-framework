package com.foco.db.autoconfigure;

import com.foco.db.transmit.IgnoreDeletedTransmit;
import com.foco.db.transmit.IgnoreTenantTransmit;
import com.foco.db.transmit.TenantTransmit;
import org.springframework.context.annotation.Bean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/27 15:03
 * @since foco2.1.0
 */
public class TransmitAutoConfiguration {
    @Bean
    IgnoreDeletedTransmit ignoreDeletedTransmit(){
        return new IgnoreDeletedTransmit();
    }
    @Bean
    IgnoreTenantTransmit ignoreTenantTransmit(){
        return new IgnoreTenantTransmit();
    }
    @Bean
    TenantTransmit tenantTransmit(){
        return new TenantTransmit();
    }
}
