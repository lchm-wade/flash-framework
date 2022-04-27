/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-03 10:57:42)
 */

package com.foco.auth.provider.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.auth.provider.db.TokenInfo;
import com.foco.db.annotation.MultiIgnoreLogicDelete;


/**
 * TokenInfoMapper
 * @author lucoo
 * @date 2020-12-03 10:57:42
 */
@MultiIgnoreLogicDelete({"delete","selectOne"})
public interface TokenInfoMapper extends BaseMapper<TokenInfo> {

}