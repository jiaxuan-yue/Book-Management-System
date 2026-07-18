package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件控制器 —— 提供文件上传和下载的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /files} 为前缀。
 * 文件存储在服务器本地磁盘的 {@code files/} 目录下（项目运行目录/files/）。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST /files/upload          → 通用文件上传（返回文件下载 URL）</li>
 *   <li>GET  /files/download/{name} → 根据文件名下载文件</li>
 *   <li>POST /files/wang/upload     → wangEditor 富文本编辑器专用文件上传接口</li>
 * </ul>
 * <p>
 * 文件命名策略：使用时间戳 + 原始文件名，避免同名文件覆盖。
 * 例如：{@code 1721299200000-avatar.png}
 * <p>
 * 上传大小限制：在 application.yml 中配置为 100MB。
 */
@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * 本地磁盘文件的存储根路径
     * <p>
     * {@code System.getProperty("user.dir")} 获取项目运行时的当前工作目录，
     * 拼接 "/files/" 作为文件上传的存储目录。
     * 例如项目运行在 /home/app/springboot，则文件存储在 /home/app/springboot/files/
     */
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    /**
     * 文件下载 URL 的前缀地址
     * <p>
     * 从 application.yml 的 {@code fileBaseUrl} 配置项读取，
     * 本地开发时为 http://localhost:9090，部署时为服务器的公网地址。
     * 上传成功后会拼接出完整的文件访问 URL 返回给前端。
     */
    @Value("${fileBaseUrl}")
    private String fileBaseUrl;

    /**
     * 通用文件上传接口
     * <p>
     * 接收前端上传的文件（支持图片、文档等任意类型），
     * 以 "时间戳-原始文件名" 的方式保存到服务器本地 files/ 目录，
     * 返回该文件的下载 URL 供前端使用（如显示图片、存入数据库等）。
     *
     * @param file 前端上传的文件对象（multipart/form-data 格式）
     * @return 包含文件下载 URL 的成功响应，如 http://localhost:9090/files/download/1721299200000-avatar.png
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // 定义文件的唯一标识
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        // 拼接完整的文件存储路径
        String realFilePath = filePath + fileName;
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), realFilePath);
        } catch (IOException e) {
            System.out.println("文件上传错误");
        }
        String url = fileBaseUrl + "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载接口
     * <p>
     * 根据文件名从本地 files/ 目录读取文件，以字节流的形式写入 HTTP 响应，
     * 供前端下载或直接在浏览器中展示（如图片）。
     * <p>
     * 前端可以直接通过 {@code <img src="http://localhost:9090/files/download/xxx.png">} 展示图片。
     *
     * @param fileName 文件名（包含时间戳前缀），如 1721299200000-avatar.png
     * @param response HTTP 响应对象，用于写入文件字节流
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        // 设置下载文件http响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        // 拼接完整的文件存储路径
        String realFilePath = filePath + fileName;
        try {
            // 通过文件的存储路径拿到文件字节数组
            byte[] bytes = FileUtil.readBytes(realFilePath);
            ServletOutputStream os = response.getOutputStream();
            // 将文件字节数组写出到文件流
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println("文件下载错误");
        }
    }

    /**
     * wangEditor 富文本编辑器专用文件上传接口
     * <p>
     * wangEditor 编辑器在插入图片时会调用此接口上传文件。
     * 与通用上传接口的区别在于返回格式不同：
     * wangEditor 要求返回特定的 JSON 结构 {@code {"errno": 0, "data": [{"url": "..."}]}}，
     * 而不是统一的 Result 格式。
     *
     * @param file 编辑器上传的图片文件
     * @return wangEditor 要求的特定格式响应
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--上传成功");
            Thread.sleep(1L);
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = fileBaseUrl + "/files/download/";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
        return resMap;
    }

}
