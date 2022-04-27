package com.foco.model.spi;

import com.foco.model.ApiResult;

public interface ApiResultBuilder {
    ApiResult build(String code, String message,Object... params);
}
