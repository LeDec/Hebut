package com.android.quest.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.android.quest.common.ApiResponse;
import com.android.quest.common.Enum.ErrorEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/project/file")
@CrossOrigin
public class FileController {

    //private static final String PATH = "./res/tape";

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


    @PostMapping(value = "/fileTape")
    @ApiOperation(value = "上传录音")
    public ApiResponse file(
            @RequestParam("file") MultipartFile tape
    ) throws IOException {
        if (tape == null || tape.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        String fileName = tape.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        long time = System.currentTimeMillis();
        String data = "tape" + time;
        File localPath = new File("./res/tape");
        Path path = Paths.get(localPath.getAbsolutePath(), data + suffix);
        tape.transferTo(path);
        return ApiResponse.success(data + suffix);
    }
}
