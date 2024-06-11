package com.toucan.shopping.modules.ueditor.vo;

import lombok.Data;

@Data
public class ImgUploadResult {

    private String url; //图片地址
    private String state; //状态
    private String title; //标题
    private String original; //图片名

}
