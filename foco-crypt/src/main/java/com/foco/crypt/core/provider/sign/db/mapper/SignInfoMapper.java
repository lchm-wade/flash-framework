/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-16 15:22:50)
 */

package com.foco.crypt.core.provider.sign.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.crypt.core.provider.sign.db.SignInfo;
import com.foco.db.annotation.MultiIgnoreLogicDelete;

/**
 * SysCryptInfoMapper
 * @author lucoo
 * @date 2020-12-16 15:22:50
 */
@MultiIgnoreLogicDelete("selectOne")
public interface SignInfoMapper extends BaseMapper<SignInfo> {

}