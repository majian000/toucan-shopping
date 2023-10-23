package com.toucan.shopping.modules.designer.core.view;

import lombok.Data;

/**
 * 组件视图(决定组件的显示)
 * @author majian
 */
@Data
public abstract class ComponentView {

    private String type; //组件类型

    private String title; //标题
    private String backgroundColor; //背景色

    private String width; //宽度
    private String height;  //高度

    //坐标
    private String x;
    private String y;


}
