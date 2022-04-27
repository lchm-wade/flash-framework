package com.foco.model.spi;

import java.util.List;

public interface ExcludeAutoConfigure {
     void exclude(List<String> excludeList,Object environment);
}
