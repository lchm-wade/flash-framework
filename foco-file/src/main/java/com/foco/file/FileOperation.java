package com.foco.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.foco.model.ApiResult;
import com.foco.model.exception.SystemException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/28 14:41
 **/
@RestController
@RequestMapping("/file")
@Slf4j
public class FileOperation {
    @Autowired
    private FileService service;
    @Autowired(required = false)
    private FileIntercept fileIntercept;
    String separator = System.getProperty("file.separator");

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "module", value = "模块名称,为不同业务模块创建文件夹"),
                    @ApiImplicitParam(name = "autoCreateName", value = "是否自动创建文件名,不传则或者true则自动创建文件名,false则使用文件本身的文件名")}
    )
    public ApiResult upload(@RequestParam("file") MultipartFile file, String module, Boolean autoCreateName) {
        if (fileIntercept != null) {
            fileIntercept.onUpload(file, module);
        }
        FileInfo fileInfo = new FileInfo();
        try {
            fileInfo.setContent(file.getBytes());
        } catch (IOException e) {
            SystemException.throwException("上传异常", e);
        }
        String date = LocalDate.now().toString().replaceAll("-", "");
        String fileName = file.getOriginalFilename();
        if (autoCreateName == null || autoCreateName) {
            String fileExtName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + fileExtName;
        }
        if (StrUtil.isNotBlank(module)) {
            module = module + separator;
        }
        String filePath = separator+module+ date + separator+ fileName;
        fileInfo.setFilePath(filePath);
        String key = service.uploadFile(fileInfo);
        return ApiResult.success(key);
    }

    @PostMapping("/down")
    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "path", value = "上传接口返回的文件路径", required = true)
    public void down(HttpServletResponse response, HttpServletRequest request, @RequestParam String path) {
        if (fileIntercept != null) {
            fileIntercept.onDown(path);
        }
        try {
            // 取得文件名。
            String[] split = path.split("/");
            String fileName = split[split.length - 1];
            log.info("下载的文件名称:{}", fileName);
            String filenameEncoder = decodeFileName(fileName, request);
            // 以流的形式下载文件。
            byte[] buffer = service.downFile(path);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;fileName=\"" + filenameEncoder + "\"");
            response.addHeader("Content-Length", "" + buffer.length);
            response.setContentType("application/octet-stream");
            try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
                toClient.write(buffer);
                toClient.flush();
            }
        } catch (Exception ex) {
           SystemException.throwException("下载异常", ex);
        }
    }

    @PostMapping("/downZip")
    @ApiOperation("下载文件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paths", value = "上传接口返回的文件路径,多个以逗号隔开", required = true),
            @ApiImplicitParam(name = "zipName", value = "下载后的包名,不指定取时间戳")
    })

    public void downZip(HttpServletRequest request, HttpServletResponse response, @RequestParam String paths, String zipName) {
        String[] pathArray = paths.split(",");
        if (fileIntercept != null) {
            for (String path : pathArray) {
                fileIntercept.onDown(path);
            }
        }
        if (StrUtil.isBlank(zipName)) {
            zipName = System.currentTimeMillis() + ".zip";
        } else {
            zipName = decodeFileName(zipName, request)+".zip";
        }
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + zipName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            log.error("文件打包下载异常",e);
        }
        //循环将文件写入压缩流
        DataOutputStream os = null;

        for (String url : pathArray) {
            try {
                byte[] b = service.downFile(url);
                String filename = FileUtil.getName(url);
                zipos.putNextEntry(new ZipEntry(filename));
                os = new DataOutputStream(zipos);
                os.write(b, 0, b.length);
                zipos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭流
        try {
            os.flush();
            os.close();
            zipos.close();
        } catch (IOException e) {
            log.error("文件打包下载异常",e);
        }
    }

    private String decodeFileName(String fileName, HttpServletRequest request){
        String agent = request.getHeader("USER-AGENT");
        try {
            //针对IE或者以IE为内核的浏览器：
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            log.error("文件名称解码异常",e);
        }
        return fileName;
    }
}
