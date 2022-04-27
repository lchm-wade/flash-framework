package com.foco.crypt.autoconfigure;

import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.algorithm.crypt.CryptAlgorithmFactory;
import com.foco.crypt.core.algorithm.sign.SignAlgorithmFactory;
import com.foco.crypt.core.provider.crypt.CryptConfigProvider;
import com.foco.crypt.core.provider.crypt.DefaultCryptConfigProvider;
import com.foco.crypt.core.provider.crypt.LocalCryptConfigProvider;
import com.foco.crypt.core.provider.crypt.RedisCryptConfigProvider;
import com.foco.crypt.core.provider.crypt.db.DbCryptConfigProvider;
import com.foco.crypt.core.provider.sign.DefaultSignConfigProvider;
import com.foco.crypt.core.provider.sign.LocalSignConfigProvider;
import com.foco.crypt.core.provider.sign.RedisSignConfigProvider;
import com.foco.crypt.core.provider.sign.SignConfigProvider;
import com.foco.crypt.core.provider.sign.db.DbSignConfigProvider;
import com.foco.model.constant.FocoConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:12
 **/
@Configuration
@ComponentScan(FocoConstants.MAIN_PACKAGE_CRYPT)
@EnableConfigurationProperties({CryptProperties.class, SignProperties.class})
public class CryptConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "foco.crypt", name = "store", havingValue = "REDIS")
    CryptConfigProvider redisCryptConfigProvider() {
        return new RedisCryptConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.crypt", name = "store", havingValue = "DB")
    CryptConfigProvider dbCryptConfigProvider() {
        return new DbCryptConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.crypt", name = "store",havingValue = "LOCAL",matchIfMissing = true)
    CryptConfigProvider localCryptConfigManager() {
        return new LocalCryptConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.crypt", name = "store", havingValue = "DEFAULT")
    CryptConfigProvider defaultCryptConfigProvider() {
        return new DefaultCryptConfigProvider();
    }
    @Bean
    CryptAlgorithmFactory cryptAlgorithmFactory() {
        return new CryptAlgorithmFactory();
    }

    @Bean
    CryptHandler cryptHandler() {
        return new CryptHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.sign", name = "store", havingValue = "REDIS")
    SignConfigProvider redisSignConfigProvider() {
        return new RedisSignConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.sign", name = "store", havingValue = "DB")
    SignConfigProvider dbSignConfigProvider() {
        return new DbSignConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.sign", name = "store", havingValue = "LOCAL",matchIfMissing = true)
    SignConfigProvider localSignConfigProvider() {
        return new LocalSignConfigProvider();
    }

    @Bean
    @ConditionalOnProperty(prefix = "foco.sign", name = "store", havingValue = "DEFAULT")
    SignConfigProvider defaultSignConfigProvider() {
        return new DefaultSignConfigProvider();
    }

    @Bean
    SignAlgorithmFactory signAlgorithmFactory() {
        return new SignAlgorithmFactory();
    }
    @Bean
    SignHandler signHandler() {
        return new SignHandler();
    }
}
