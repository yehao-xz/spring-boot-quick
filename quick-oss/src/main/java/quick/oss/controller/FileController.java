package quick.oss.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import quick.oss.utils.OSSUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件
 *
 * @author yehao
 * @date 2021/8/17
 */
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        return OSSUtil.uploadObject(file, "yehao-bucket", "test-file/");
    }

    @PostMapping("/delete")
    public void delete(@RequestParam("objectName") String objectName) {
        OSSUtil.deleteFile("yehao-bucket", objectName);
    }

    @GetMapping("/download")
    public void download(@RequestParam("objectName") String objectName, HttpServletResponse response) {
        String suffix = objectName.substring(objectName.lastIndexOf("."));
        OSSUtil.downloadObject("yehao-bucket", objectName, StrUtil.uuid() + "." + suffix, response);
    }
}
