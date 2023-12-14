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

    private String clickPath; //点击路径

    public ImageComponent(String imgPath, String httpImgPath, String imgRefId, String clickPath) {
        this.imgPath = imgPath;
        this.httpImgPath = httpImgPath;
        this.imgRefId = imgRefId;
        this.clickPath = clickPath;
    }

    public ImageComponent() {

    }

    public String getClickPath() {
        return clickPath;
    }

    public void setClickPath(String clickPath) {
        this.clickPath = clickPath;
    }

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
