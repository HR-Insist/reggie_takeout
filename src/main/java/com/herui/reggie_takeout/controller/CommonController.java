package com.herui.reggie_takeout.controller;

import com.herui.reggie_takeout.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

import static java.lang.System.out;

/**
 * 文件上传下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        log.info("文件上传: ", file.toString());
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID
        String fileName = UUID.randomUUID().toString() + suffix;
        try{
            // 保存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("文件上传成功", fileName);
    }

    @GetMapping("/download")
    public void download(@RequestParam String name, HttpServletResponse response) throws IOException {
        // 输入流 读取文件内容
        try(FileInputStream fileInputStream = new FileInputStream(basePath+name);) {
            response.setContentType("image/png,image/jpeg,image/jpg");// 设置MIME类型，也就是响应类型
            // 输出流， 通过输出流文件协会浏览器，在浏览器展示
            ServletOutputStream outputStream = response.getOutputStream();
            int available = fileInputStream.available();
            byte[] bytes = new byte[available];
            int read = fileInputStream.read(bytes);
            outputStream.write(bytes);

            outputStream.flush();
            fileInputStream.close();
            outputStream.close();

        }
        catch (Exception e){
            e.printStackTrace();
//            return Result.error("error");
        }
    }
}
