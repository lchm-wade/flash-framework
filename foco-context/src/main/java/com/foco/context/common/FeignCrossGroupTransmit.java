package com.foco.context.common;

import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description fein 跨组调用时透传参数
 * @date 2021/12/08 10:39
 * @since foco2.1.0
 */
public interface FeignCrossGroupTransmit {
    List<KVPair> get();
    class KVPair{
        String key;
        String value;

        public KVPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
