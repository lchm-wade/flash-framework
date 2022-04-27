package com.foco.model.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ExcludeAutoConfigureManager {
    public static List<ExcludeAutoConfigure> getExcludeAutoConfigures(){
        List<ExcludeAutoConfigure> list=new ArrayList<>();
        ServiceLoader<ExcludeAutoConfigure> excludeAutoConfigures = ServiceLoader.load(ExcludeAutoConfigure.class);
        for (ExcludeAutoConfigure api : excludeAutoConfigures) {
            list.add(api);
        }
        return list;
    }
}
