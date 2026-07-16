package com.toucan.shopping.modules.minio.service;

import com.toucan.shopping.modules.common.file.ByteArrayMultipartFile;
import com.toucan.shopping.modules.minio.config.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * MinIO 文件服务实现
 */
@Service
public class MinioServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 公开文件桶 */
    public static final String BUCKET_NAME_PUBLIC_FILE = "public-file";

    /** 私有文件桶 */
    public static final String BUCKET_NAME_PRIVATE_FILE = "private-file";

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    // ======================== 上传 ========================

    /**
     * 上传文件到默认桶（private-file）
     */
    public String upload(MultipartFile file) throws Exception {
        return upload(file, BUCKET_NAME_PRIVATE_FILE);
    }

    /**
     * 上传文件到指定桶
     */
    public String upload(MultipartFile file, String bucketName) throws Exception {
        String fileName = generateFileName(file.getOriginalFilename());
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .method(Method.GET)
                            .build());
            url = url.substring(0, url.indexOf('?'));
            return URLDecoder.decode(url, StandardCharsets.UTF_8);
        }
    }

    /**
     * Base64 图片上传到默认桶，返回相对路径
     */
    public String uploadBase64Image(String base64Data) throws Exception {
        return uploadBase64Image(base64Data, BUCKET_NAME_PRIVATE_FILE);
    }

    /**
     * Base64 图片上传到指定桶，返回相对路径
     */
    public String uploadBase64Image(String base64Data, String bucketName) throws Exception {
        String[] parts = base64Data.split(",");
        byte[] imageBytes = Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);
        String type = detectImageType(parts.length > 1 ? parts[1] : parts[0]);
        String fileName = UUID.randomUUID().toString().replace("-", "") + type;
        String contentType = ".png".equals(type) ? "image/png" :
                ".jpg".equals(type) ? "image/jpeg" : "application/octet-stream";
        ByteArrayMultipartFile file = new ByteArrayMultipartFile("file", fileName, contentType, imageBytes);
        String fullUrl = upload(file, bucketName);
        String prefix = minioConfig.getUrl() + "/" + bucketName;
        return fullUrl.replace(prefix, "");
    }

    /**
     * Base64 图片上传到指定桶，返回完整路径
     */
    public String uploadBase64ImageFullUrl(String base64Data, String bucketName) throws Exception {
        String[] parts = base64Data.split(",");
        byte[] imageBytes = Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);
        String type = detectImageType(parts.length > 1 ? parts[1] : parts[0]);
        String fileName = UUID.randomUUID().toString().replace("-", "") + type;
        String contentType = ".png".equals(type) ? "image/png" :
                ".jpg".equals(type) ? "image/jpeg" : "application/octet-stream";
        ByteArrayMultipartFile file = new ByteArrayMultipartFile("file", fileName, contentType, imageBytes);
        return upload(file, bucketName);
    }

    /**
     * Base64 PDF 上传到默认桶，返回相对路径
     */
    public String uploadBase64Pdf(String base64Data) throws Exception {
        byte[] pdfBytes = Base64.getDecoder().decode(base64Data);
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".pdf";
        ByteArrayMultipartFile file = new ByteArrayMultipartFile("file", fileName, "application/pdf", pdfBytes);
        String fullUrl = upload(file, BUCKET_NAME_PRIVATE_FILE);
        String prefix = minioConfig.getUrl() + "/" + BUCKET_NAME_PRIVATE_FILE;
        return fullUrl.replace(prefix, "");
    }

    // ======================== 下载 ========================

    /**
     * 下载文件为字节数组
     */
    public byte[] downloadAsByte(String bucketName, String objectName) throws Exception {
        objectName = stripLeadingSlash(objectName);
        try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName).object(objectName).build());
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int n;
            while ((n = is.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
            return bos.toByteArray();
        }
    }

    /**
     * 下载文件为 Base64 字符串
     */
    public String downloadAsBase64(String bucketName, String objectName) throws Exception {
        byte[] bytes = downloadAsByte(bucketName, objectName);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 下载文件写入 HttpServletResponse
     */
    public void downloadToResponse(String objectName, String bucketName, HttpServletResponse response) throws Exception {
        try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName).object(objectName).build())) {
            String filename = new String(objectName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf)) > 0) {
                out.write(buf, 0, n);
            }
            out.flush();
        }
    }

    // ======================== 工具方法 ========================

    /**
     * 获取文件输入流
     */
    public InputStream getFileInputStream(String objectName, String bucketName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            logger.error("获取文件流失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取文件大小（字节）
     */
    public Long getFileSize(String objectName, String bucketName) {
        try {
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName).object(objectName).build());
            return stat.size();
        } catch (Exception e) {
            logger.error("获取文件大小失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 删除文件
     */
    public boolean delete(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName).object(fileName).build());
            return true;
        } catch (Exception e) {
            logger.error("删除文件失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取文件访问 URL（不带签名）
     */
    public String getFileUrl(String bucketName, String objectName) {
        String baseUrl = minioConfig.getUrlView() != null ? minioConfig.getUrlView() : minioConfig.getUrl();
        return baseUrl + "/" + bucketName + "/" + objectName;
    }

    /**
     * 获取公开桶的完整访问地址前缀
     */
    public String getPublicBucketBaseUrl() {
        String baseUrl = minioConfig.getUrlView() != null ? minioConfig.getUrlView() : minioConfig.getUrl();
        return baseUrl + "/" + BUCKET_NAME_PUBLIC_FILE;
    }

    /**
     * 获取私有桶的完整访问地址前缀
     */
    public String getPrivateBucketBaseUrl() {
        String baseUrl = minioConfig.getUrlView() != null ? minioConfig.getUrlView() : minioConfig.getUrl();
        return baseUrl + "/" + BUCKET_NAME_PRIVATE_FILE;
    }

    /**
     * 获取公开桶名称
     */
    public String getPublicBucketName() {
        return BUCKET_NAME_PUBLIC_FILE;
    }

    /**
     * 获取私有桶名称
     */
    public String getPrivateBucketName() {
        return BUCKET_NAME_PRIVATE_FILE;
    }

    /**
     * 生成预签名访问 URL（临时授权，默认1小时有效）
     */
    public String getPresignedUrl(String bucketName, String objectName) {
        return getPresignedUrl(bucketName, objectName, 3600);
    }

    /**
     * 生成预签名访问 URL，指定过期秒数
     */
    public String getPresignedUrl(String bucketName, String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(stripLeadingSlash(objectName))
                            .method(Method.GET)
                            .expiry(expirySeconds)
                            .build());
        } catch (Exception e) {
            logger.error("生成预签名URL失败: {}", e.getMessage());
            return getFileUrl(bucketName, objectName);
        }
    }

    // ======================== 私有方法 ========================

    private String generateFileName(String originalFilename) {
        String datePath = java.time.LocalDate.now().toString().replace("-", "/");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return datePath + "/" + uuid + ext;
    }

    private String detectImageType(String base64) {
        byte[] b = Base64.getDecoder().decode(base64);
        if (b.length >= 2) {
            int magic = ((b[0] & 0xff) << 8) | (b[1] & 0xff);
            if (magic == 0x8950) return ".png";
            if (magic == 0xFFD8) return ".jpg";
            if (magic == 0x424D) return ".bmp";
        }
        return ".jpg";
    }

    private String stripLeadingSlash(String path) {
        return path != null && path.startsWith("/") ? path.substring(1) : path;
    }


}
