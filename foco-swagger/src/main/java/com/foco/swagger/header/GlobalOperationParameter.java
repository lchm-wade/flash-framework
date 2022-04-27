package com.foco.swagger.header;

import lombok.Data;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/11 14:42
 */
@Data
public  class GlobalOperationParameter {
    private String name;
    private String description;
    private String parameterType;
    private String modelRef;
    private Boolean required;
}
