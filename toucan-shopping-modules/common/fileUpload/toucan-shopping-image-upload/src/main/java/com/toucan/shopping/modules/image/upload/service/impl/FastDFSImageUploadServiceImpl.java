package com.toucan.shopping.modules.image.upload.service.impl;

import com.toucan.shopping.modules.fastdfs.util.FastDFSClient;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class FastDFSImageUploadServiceImpl implements ImageUploadService {


    @Value("${fastdfs.http.url}")
    private String fastDfsHttpUrl;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Override
    public String uploadFile(byte[] fileContent, String extName) {
        return fastDFSClient.uploadFile(fileContent, extName);
    }

    @Override
    public Integer deleteFile(String storagePath) {
        return fastDFSClient.delete_file(storagePath);
    }

    @Override
    public byte[] downloadFile(String storagePath) {
        return fastDFSClient.download_bytes(storagePath);
    }

    @Override
    public String getImageHttpPrefix() {
        return fastDfsHttpUrl;
    }

    private static final java.util.Set<String> ALLOWED_IMAGE_TYPES = new java.util.HashSet<>(
            java.util.Arrays.asList("jpg", "jpeg", "png", "gif", "bmp"));

    @Override
    public String uploadBase64(String base64Data) throws Exception {
        if (StringUtils.isEmpty(base64Data) || !base64Data.startsWith("data:image/")) {
            throw new RuntimeException("无效的base64图片格式");
        }
        String[] parts = base64Data.split(",");
        if (parts.length != 2) {
            throw new RuntimeException("无效的base64图片格式");
        }
        // 从header提取扩展名: data:image/png;base64 -> png
        String header = parts[0];
        String fileExt = "jpg";
        if (header.contains("image/")) {
            String mime = header.substring(header.indexOf("image/") + 6);
            if (mime.contains(";")) {
                mime = mime.substring(0, mime.indexOf(";"));
            }
            // image/jpeg -> jpg
            if ("jpeg".equalsIgnoreCase(mime)) {
                fileExt = "jpg";
            } else if (ALLOWED_IMAGE_TYPES.contains(mime.toLowerCase())) {
                fileExt = mime.toLowerCase();
            } else {
                throw new RuntimeException("不支持的图片格式，仅支持JPG、JPEG、PNG、GIF、BMP");
            }
        }
        byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
        return uploadFile(imageBytes, fileExt);
    }
}
