package com.Hebut.JiangXin.project.controller;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Lenovo
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@Api(tags = "文件")
public class FileController {

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param response 下载请求
     */
    @PostMapping(value = "/download")
    @ApiOperation(value = "文件下载")
    public static void downloadFile(String fileName, String filePath, HttpServletResponse response) {
        response.setContentType("application/octet-stream; charset=UTF-8");
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            File file = new File(filePath, fileName);
            // 判断文件是否存在
            if (!file.exists()) {
                JSONObject res = new JSONObject();
                // 文件不存在
                res.put("code", ErrorEnum.INNER_ERROR.getCode());
                res.put("msg", "文件不存在");
                PrintWriter writer = response.getWriter();
                writer.append(res.toString());
            } else {
                // 获取文件输入流
                FileInputStream inputStream = new FileInputStream(file);
                String encodeFileName = new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1);
                response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
                response.setHeader("Content-Length", String.valueOf(inputStream.getChannel().size()));
                os = response.getOutputStream();
                bis = new BufferedInputStream(inputStream);
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


