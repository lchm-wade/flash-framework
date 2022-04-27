package com.foco.file;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/28 10:49
 **/
@Getter
@Setter
public class FileInfo {
    /**
     * 文件路径,可以分业务模块和时间等维度做目录
     */
    private String filePath;
    /**
     * 文件内容
     */
    private byte[] content;
}
