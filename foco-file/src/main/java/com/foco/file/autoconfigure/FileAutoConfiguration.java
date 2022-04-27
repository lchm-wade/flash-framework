package com.foco.file.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.file.FileService;
import com.foco.file.properties.FileProperties;
import com.foco.file.provider.LocalFileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/28 14:49
 **/
@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-file",this.getClass());
    }
    @ConditionalOnProperty(prefix = "foco.file",name = "store",havingValue = "local",matchIfMissing = true)
    @Bean
    public FileService fileService(){
        return new LocalFileService();
    }
}
