package quick.fastdfs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import quick.fastdfs.utils.FastdfsUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理
 *
 * @author yehao
 * @date 2021/8/12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FastdfsUtils fastdfsUtils;

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fastdfsUtils.upload(file);
    }

    @PostMapping("/delete")
    public Object delete(@RequestParam("path") String path) {
        fastdfsUtils.delete(path);
        return "ok";
    }

    @GetMapping("/download")
    public Object download(@RequestParam("path") String path, HttpServletResponse response) throws Exception {
        fastdfsUtils.download(path, "doenload-file", response);
        return "ok";
    }
}

