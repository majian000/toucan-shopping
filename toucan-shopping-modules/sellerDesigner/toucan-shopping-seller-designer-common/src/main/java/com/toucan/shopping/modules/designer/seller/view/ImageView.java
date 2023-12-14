package com.toucan.shopping.modules.designer.seller.view;

import com.toucan.shopping.modules.designer.core.view.ComponentView;
import lombok.Data;

/**
 * 图片组件视图
 * @author majian
 */
public class ImageView extends ComponentView {

    private String src; //图片路径

    private String clickPath; //单击路径

    public String getClickPath() {
        return clickPath;
    }

    public void setClickPath(String clickPath) {
        this.clickPath = clickPath;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
