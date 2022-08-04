package com.toucan.shopping.cloud.apps.admin.vo.image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImgUploadVO {

    private String path; //图片路径

    private String httpPath; //外部访问路径

}
