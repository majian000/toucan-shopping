package com.toucan.shopping.modules.designer.seller.model.component;


import com.toucan.shopping.modules.designer.core.model.component.BaseComponent;
import lombok.Data;

/**
 * 图片组件
 * @author majian
 */
public class ImageComponent extends BaseComponent {

    private String imgPath; //图片路径

    private String httpImgPath; //图片预览路径

    private String imgRefId; //图片ID


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getHttpImgPath() {
        return httpImgPath;
    }

    public void setHttpImgPath(String httpImgPath) {
        this.httpImgPath = httpImgPath;
    }

    public String getImgRefId() {
        return imgRefId;
    }

    public void setImgRefId(String imgRefId) {
        this.imgRefId = imgRefId;
    }
}
