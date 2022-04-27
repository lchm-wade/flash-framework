package com.foco.context.core;

import com.foco.model.constant.MainClassConstant;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/4 14:31
 **/
public class DataSourceContextHolder {
    public static final String masterDsName="master";
    @Deprecated
    public static final String shadowDsName="shadow";
    //存放当前线程使用的数据源类型信息
    private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();
    //存放数据源id
    private static List<String> dataSourceIds = new ArrayList<String>();

    public static List<String> getDataSources() {
        return dataSourceIds;
    }


    public static void master(){
        setDataSource(masterDsName);
    }
    @Deprecated
    public static void shadow(){
        contextHolder.set(shadowDsName);
    }

    //设置数据源
    public static void setDataSource(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }
    //获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    //清除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }
    //清除数据源
    @Deprecated
    public static void clearShadowDataSource() {
        contextHolder.remove();
    }
    //判断当前数据源是否存在
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }

    public static void addDataSourceId(String dataSourceId){
        dataSourceIds.add(dataSourceId);
    }

    /**
     * 临时切换主数据源
     */
    public static <T> T switchMasterTemporary(Supplier<T> supplier){
        if(ClassUtils.isPresent(MainClassConstant.FOCO_DYNAMIC_SOURCE,DataSourceContextHolder.class.getClassLoader())){
            String dataSource = getDataSource();
            try {
                master();
                return supplier.get();
            }catch (Exception e){
                throw e;
            }finally {
                DataSourceContextHolder.clearDataSource();
                DataSourceContextHolder.setDataSource(dataSource);
            }
        }
        return supplier.get();
    }
}
