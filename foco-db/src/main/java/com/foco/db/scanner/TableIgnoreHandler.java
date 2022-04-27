package com.foco.db.scanner;
import java.util.HashSet;
import java.util.Set;
/**
 * @author lucoo
 * @version 1.0.0
 * @description foco体系的表增强忽略
 * @date 2021/12/23 11:11
 * @since foco2.3.0
 */
public class TableIgnoreHandler{
    private static Set<String> ignoreTables=new HashSet<>();

    public static Set<String> getIgnoreTables() {
        return ignoreTables;
    }
    public static void addTables(String tableName){
        ignoreTables.add(tableName.replaceAll("`",""));
    }
}
