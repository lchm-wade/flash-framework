package com.foco.mq.extend;

/**
 * @author ChenMing
 * @date 2021/11/7
 */
public interface ProductionOrdered {

    /**
     * 排序，数值越小，先执行
     * @return 默认0
     */
    default int productionOrder() {
        return 0;
    }
}
