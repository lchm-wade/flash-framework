/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-16 15:22:50)
 */

package com.foco.crypt.core.provider.crypt.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.crypt.core.provider.crypt.db.CryptInfo;
import com.foco.db.annotation.MultiIgnoreLogicDelete;

/**
 * SysCryptInfoMapper
 * @author lucoo
 * @date 2020-12-16 15:22:50
 */
@MultiIgnoreLogicDelete("selectOne")
public interface CryptInfoMapper extends BaseMapper<CryptInfo> {

}