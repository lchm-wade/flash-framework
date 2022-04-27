package com.foco.file.provider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.foco.file.properties.FileProperties;
import com.foco.file.FileInfo;
import com.foco.file.FileService;
import com.foco.model.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Description: 本地存储
 * @Author lucoo
 * @Date 2021/6/28 10:54
 **/
public class LocalFileService implements FileService {
    @Autowired
    private FileProperties fileProperties;
    String separator = System.getProperty("file.separator");

    @Override
    public String uploadFile(FileInfo fileInfo) {
        checkFile(fileInfo);
        String filePath = handlerPath(fileInfo.getFilePath());
        String storePath = handlerPath(fileProperties.getStorePath());
        String fileKey = separator+"node" + fileProperties.getNodeId() + filePath;
        String path = storePath + fileKey;
        //创建文件
        if (!FileUtil.exist(path)) {
            try {
                FileUtil.touch(path);
            } catch (Exception e) {
                SystemException.throwException("文件路径错误:" + path);
            }
        }
        Path fPath = Paths.get(path);
        //创建BufferedWriter
        try (OutputStream bfw = Files.newOutputStream(fPath)) {
            bfw.write(fileInfo.getContent());
            bfw.flush();
        } catch (IOException e) {
            SystemException.throwException("上传文件异常", e);
        }
        return fileKey;
    }

    @Override
    public byte[] downFile(String path) {
        String url = fileProperties.getAccessUrl() + path;
        try {
            URL link = new URL(url);
            try (InputStream is = new BufferedInputStream(link.openStream());) {
                return IoUtil.readBytes(is);
            } catch (Exception e) {
                throw new SystemException("文件下载异常", e);
            }
        } catch (MalformedURLException e) {
            throw new SystemException(String.format("文件下载异常,URL不合法%s",url));
        }
    }

    private void checkFile(FileInfo fileInfo) {
        if (fileInfo == null) {
            SystemException.throwException("fileInfo不能为空");
        }
        if (StrUtil.isBlank(fileInfo.getFilePath())) {
            SystemException.throwException("filePath不能为空");
        }
        if (fileInfo.getContent().length == 0) {
            SystemException.throwException("content不能为空");
        }
        if (fileInfo.getContent().length > fileProperties.getMaxSize()) {
            SystemException.throwException("content大小超过最大限制");
        }

    }

    private String handlerPath(String path) {
        String finalPath = path.replace("/",separator);
        if (!finalPath.startsWith(separator)) {
            finalPath = separator + finalPath;
        }
        if (finalPath.endsWith(separator)) {
            finalPath = finalPath.substring(0, finalPath.length() - 1);
        }
        return finalPath;
    }
}
