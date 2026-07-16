# MinIO模块

    MinIO操作实现类

## 配置示例

在 `application.yml` 中添加以下配置：

```yaml
minio:
  # MinIO 服务地址
  url: http://127.0.0.1:9000
  # 访问密钥
  access-key: minioadmin
  # 私有密钥
  secret-key: minioadmin
  # 默认桶名称
  bucket-name: default-bucket
  # 公网访问地址（可选，用于拼接文件访问URL，不配置则使用url）
  url-view: http://127.0.0.1:9000
```
