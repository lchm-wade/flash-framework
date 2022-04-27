package com.foco.db.util;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/27 14:47
 * @since foco2.1.0
 */
public class TenantInfo {
    private String columnName;
    private String columnValue;
    private ColumnType columnType;
    public enum ColumnType{
        STRING,LONG,DOUBLE
    }
    public TenantInfo(String columnName, String columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.columnType = ColumnType.LONG;
    }
    public TenantInfo(String columnName, String columnValue, ColumnType columnType) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.columnType = columnType;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
}
