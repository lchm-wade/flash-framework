package com.foco.page.mvc.properties;

import com.foco.model.constant.FocoConstants;
import com.foco.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/2 14:43
 **/
@ConfigurationProperties(prefix = PageProperties.PREFIX)
@Getter
@Setter
public class PageProperties extends AbstractProperties {
    public boolean enabled=true;
    public static final String PREFIX= FocoConstants.CONFIG_PREFIX+"page";
    public static PageProperties getConfig(){
        return getConfig(PageProperties.class);
    }
    /**
     * 分页 每页显示最大条数 默认100条
     */
    private Integer maxPageSize=100;
}
