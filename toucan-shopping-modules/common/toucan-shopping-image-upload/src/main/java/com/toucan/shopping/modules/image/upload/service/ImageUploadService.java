package com.toucan.shopping.modules.image.upload.service;

public interface ImageUploadService {

    /**
     * 上传图片
     * @param fileContent
     * @param extName
     * @return
     */
    String uploadFile(byte[] fileContent, String extName);

    /**
     * 根据路径删除图片
     * @param storagePath
     * @return
     */
    Integer deleteFile(String storagePath);

    /**
     * 返回图片访问前缀
     * @return
     */
    String getImageHttpPrefix();
}
