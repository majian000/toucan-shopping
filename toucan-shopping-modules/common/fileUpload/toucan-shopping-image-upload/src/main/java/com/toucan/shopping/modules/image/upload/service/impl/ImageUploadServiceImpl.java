package com.toucan.shopping.modules.image.upload.service.impl;

import com.toucan.shopping.modules.fastdfs.util.FastDFSClient;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {


    @Value("${fastdfs.http.url}")
    private String fastDfsHttpUrl;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Override
    public String uploadFile(byte[] fileContent, String extName) {
        return fastDFSClient.uploadFile(fileContent,extName);
    }

    @Override
    public Integer deleteFile(String storagePath) {
        return  fastDFSClient.delete_file(storagePath);
    }

    @Override
    public String getImageHttpPrefix() {
        return fastDfsHttpUrl;
    }
}
