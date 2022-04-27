package com.foco.crypt.core.provider.crypt.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foco.crypt.properties.CryptProperties;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.provider.crypt.AbstractExternalStoreCryptConfigProvider;
import com.foco.crypt.core.provider.crypt.db.mapper.CryptInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class DbCryptConfigProvider extends AbstractExternalStoreCryptConfigProvider {
    @Autowired
    private CryptInfoMapper cryptInfoMapper;
    @Override
    protected CryptProperties loadCryptConfig(CryptProperties cryptProperties, CryptRequest cryptRequest) {
        LambdaQueryWrapper<CryptInfo> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(CryptInfo::getAppId,cryptRequest.getAppId());
        CryptInfo cryptInfo=cryptInfoMapper.selectOne(queryWrapper);

        CryptProperties properties=new CryptProperties();
        BeanCopierEx.copyProperties(cryptProperties,properties);
        if(cryptInfo!=null){
            BeanCopierEx.copyProperties(cryptInfo,properties);
        }
        return properties;
    }
}
