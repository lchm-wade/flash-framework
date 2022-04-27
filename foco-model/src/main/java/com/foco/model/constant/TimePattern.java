package com.foco.model.constant;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/06 11:10
 */
public enum TimePattern {
    FULL_DAY("yyyyMMdd"),
    FULL_HOUR("yyyyMMddHH"),
    FULL_MONTH("yyyyMM"),
    DAY("yyMMdd"),
    HOUR("yyMMddHH"),
    MONTH("yyMM"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");
    private String value;

    TimePattern(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
