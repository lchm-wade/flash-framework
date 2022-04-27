package com.foco.file;

import com.foco.model.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

/**
  * @Description: 文件上传下载之前的拦截操作
  * @Param:
  * @Return: 如果不符合条件的就直接抛BusinessException异常
  * @Author: lucoo
  * @Date:  2021/6/29 9:45
 **/
public interface FileIntercept {
    void onUpload(MultipartFile file, String module) throws ApiException;
    void onDown(String path) throws ApiException;
}
