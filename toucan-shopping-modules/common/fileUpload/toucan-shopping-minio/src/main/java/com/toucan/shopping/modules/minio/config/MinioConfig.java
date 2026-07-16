package com.toucan.shopping.modules.minio.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 配置
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    /** MinIO 服务地址 */
    private String url;

    /** 访问密钥 */
    private String accessKey;

    /** 私有密钥 */
    private String secretKey;

    /** 默认桶名称 */
    private String bucketName;

    /** 公网访问地址（可选，用于拼接文件访问URL） */
    private String urlView;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUrlView() {
        return urlView;
    }

    public void setUrlView(String urlView) {
        this.urlView = urlView;
    }
}
