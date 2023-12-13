package com.toucan.shopping.modules.designer.seller.model.component;


import com.toucan.shopping.modules.designer.core.model.component.BaseComponent;
import lombok.Data;

/**
 * 图片组件
 * @author majian
 */
@Data
public class ImageComponent extends BaseComponent {

    private String imgPath; //图片路径

    private String httpImgPath; //图片预览路径

    private String imgRefId; //图片ID



}
