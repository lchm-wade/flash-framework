package com.foco.crypt.core.provider.sign.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foco.crypt.properties.SignProperties;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.SignRequest;
import com.foco.crypt.core.provider.sign.AbstractExternalStoreSignConfigProvider;
import com.foco.crypt.core.provider.sign.db.mapper.SignInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class DbSignConfigProvider extends AbstractExternalStoreSignConfigProvider {
    @Autowired
    private SignInfoMapper signInfoMapper;

    @Override
    protected SignProperties loadSignConfig(SignProperties signProperties, SignRequest signRequest) {
        LambdaQueryWrapper<SignInfo> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(SignInfo::getAppId,signRequest.getAppId());
        SignInfo signInfo=signInfoMapper.selectOne(queryWrapper);

        SignProperties properties=new SignProperties();
        BeanCopierEx.copyProperties(signProperties,properties);
        if(signInfo!=null){
            BeanCopierEx.copyProperties(signInfo,properties);
        }
        return properties;
    }
}
