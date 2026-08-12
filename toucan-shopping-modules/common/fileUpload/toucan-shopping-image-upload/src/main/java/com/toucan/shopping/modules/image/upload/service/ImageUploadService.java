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
     * 上传base64图片，解码后存到文件服务
     * @param base64Data 以data:image/开头的base64字符串
     * @return 存储路径
     */
    String uploadBase64(String base64Data) throws Exception;

    /**
     * 根据路径下载文件
     * @param storagePath 存储路径
     * @return 文件字节数组
     */
    byte[] downloadFile(String storagePath);

    /**
     * 根据路径删除图片
     * @param storagePath
     * @return 0:成功
     */
    Integer deleteFile(String storagePath);

    /**
     * 返回图片访问前缀
     * @return
     */
    String getImageHttpPrefix();
}
