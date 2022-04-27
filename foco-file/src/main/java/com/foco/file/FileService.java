package com.foco.file;

public interface FileService {
    /**
     * @Description: 文件上传
     * @Param: 文件信息
     * @Author: lucoo
     * @Date: 2021/6/28 10:51
     **/
    String uploadFile(FileInfo fileInfo);

    /**
     * @Description: 文件下载
     * @Param: 文件存储路径
     * @Return: 文件内容 字节数组
     * @Author: lucoo
     * @Date: 2021/6/28 10:51
     **/
    byte[] downFile(String path);
}
