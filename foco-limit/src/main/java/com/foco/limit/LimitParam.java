package com.foco.limit;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/29 16:54
 **/
@Getter
@Setter
public class LimitParam {
    private String key;
    private int limitCount;
    private long limitTime;
}
