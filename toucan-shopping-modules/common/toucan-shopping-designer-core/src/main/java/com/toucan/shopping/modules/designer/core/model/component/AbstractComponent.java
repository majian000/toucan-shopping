package com.toucan.shopping.modules.designer.core.model.component;


import lombok.Data;

/**
 * 抽象组件
 * 所有组件都继承
 * @author majian
 */
@Data
public abstract class AbstractComponent implements IComponent {

    //坐标
    private String x;
    private String y;

    //宽高
    private String width;
    private String height;
    private String minHeight;
    private String minWidth;


    private String type; //组件类型
    private String name; //组件名称



}
