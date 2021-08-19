package quick.oss.utils;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OSSUnit
 *
 * @author yehao
 * @date 2021/8/17
 */
@Component
public class OSSUtil {

    // 内或外网域名
    private static String endpoint;

    // 密钥Access Key ID
    private static String accessKeyId;

    // 密钥Access Key Secret
    private static String accessKeySecret;

    /**
     * 获取阿里云OSS客户端对象
     */
    public static OSS getOSSClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 新建Bucket  --Bucket权限:私有
     *
     * @param bucketName bucket名称
     * @return true 新建Bucket成功
     */
    public static boolean createBucket(OSS ossClient, String bucketName) {
        Bucket bucket = ossClient.createBucket(bucketName);
        return bucketName.equals(bucket.getName());
    }

    /**
     * 删除Bucket
     *
     * @param bucketName bucket名称
     */
    public static void deleteBucket(OSS ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 文件上传
     *
     * @param file       上传文件
     * @param bucketName bucket名称
     * @param diskName   上传文件的目录  --bucket下文件的路径
     */
    public static String uploadObject(MultipartFile file, String bucketName, String diskName) {
        OSS ossClient = getOSSClient();
        String objectName = null;
        try {
            InputStream is = file.getInputStream();
            String fileName = file.getOriginalFilename();
            objectName = diskName + fileName;
            Long fileSize = file.getSize();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件
            ossClient.putObject(bucketName, objectName, is, metadata);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectName;
    }

    /**
     * 获取OSS服务器上的文件流
     *
     * @param bucketName bucket名称
     * @param objectName Bucket下的文件的路径名+文件名
     */
    public static InputStream getObject(String bucketName, String objectName) throws IOException {
        OSS ossClient = getOSSClient();
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 读取文件内容
        InputStream inputStream = ossObject.getObjectContent();
        // 关闭OSSClient
        ossClient.shutdown();
        return inputStream;
    }

    /**
     * 文件下载
     *
     * @param bucketName bucket名称
     * @param objectName Bucket下的文件的路径名+文件名
     * @param fileName   下载之后的文件名
     * @param response   response
     */
    public static void downloadObject(String bucketName, String objectName, String fileName, HttpServletResponse response) {
        BufferedInputStream input = null;
        OutputStream out = null;
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            OSS ossClient = getOSSClient();
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            input = new BufferedInputStream(ossObject.getObjectContent());

            byte[] buffBytes = new byte[1024];
            out = response.getOutputStream();
            int read = 0;
            while ((read = input.read(buffBytes)) != -1) {
                out.write(buffBytes, 0, read);
            }
            out.flush();
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用图片处理参数处理图片并保存为本地图片
     *
     * @param bucketName    Bucket名称
     * @param objectName    图片完整路径 不包含Bucket名称
     * @param localFilePath 本地文件的完整路径 如果指定的本地文件存在会覆盖 不存在则新建。
     * @param width         宽度
     * @param height        高度
     */
    public static void object2Local(String bucketName, String objectName, String localFilePath, Long width, Long height) {
        OSS ossClient = getOSSClient();
        // 设置图片宽高
        GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
        if (width != null && height != null) {
            String style = StrUtil.format("image/resize,m_fixed,w_{},h_{}", width, height);
            request.setProcess(style);
        }
        ossClient.getObject(request, new File(localFilePath));
        // 关闭OSSClient
        ossClient.shutdown();
    }

    /**
     * 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
     *
     * @param bucketName Bucket名称
     * @param objectName 图片完整路径 不包含Bucket名称
     * @return
     */
    public static boolean objectExist(String bucketName, String objectName) {
        OSS ossClient = getOSSClient();
        boolean found = ossClient.doesObjectExist(bucketName, objectName);
        // 关闭OSSClient
        ossClient.shutdown();
        return found;
    }

    /**
     * 获取文件列表
     *
     * @param bucketName Bucket名称
     * @param keyPrefix  指定前缀 null为不限制
     * @param maxKeys    指定个数 null为不限制
     * @return
     */
    public static List<String> listObjects(String bucketName, String keyPrefix, Integer maxKeys) {
        OSS ossClient = getOSSClient();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        if (maxKeys != null) {
            listObjectsRequest.withMaxKeys(maxKeys);
        }
        if (StrUtil.isNotBlank(keyPrefix)) {
            listObjectsRequest.withPrefix(keyPrefix);
        }

        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        List<String> objectsList = sums.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
        // 关闭OSSClient
        ossClient.shutdown();
        return objectsList;
    }

    /**
     * 删除文件
     *
     * @param bucketName Bucket名称
     * @param objectName 图片完整路径 不包含Bucket名称
     */
    public static void deleteFile(String bucketName, String objectName) {
        OSS ossClient = getOSSClient();
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient
        ossClient.shutdown();
    }

    /**
     * 通过文件名判断文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).replace(".", "");
        if ("bmp".equalsIgnoreCase(fileExtension)) return "image/bmp";
        if ("gif".equalsIgnoreCase(fileExtension)) return "image/gif";
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension))
            return "image/jpeg";
        if ("html".equalsIgnoreCase(fileExtension)) return "text/html";
        if ("txt".equalsIgnoreCase(fileExtension)) return "text/plain";
        if ("vsd".equalsIgnoreCase(fileExtension)) return "application/vnd.visio";
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension))
            return "application/vnd.ms-powerpoint";
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension))
            return "application/msword";
        if ("xml".equalsIgnoreCase(fileExtension)) return "text/xml";
        return "text/html";
    }

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OSSUtil.endpoint = endpoint;
    }

    @Value("${oss.access-key-id}")
    public void setAccessKeyId(String accessKeyId) {
        OSSUtil.accessKeyId = accessKeyId;
    }

    @Value("${oss.access-key-secret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OSSUtil.accessKeySecret = accessKeySecret;
    }
}