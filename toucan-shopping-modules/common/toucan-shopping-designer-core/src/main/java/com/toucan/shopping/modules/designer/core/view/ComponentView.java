package com.toucan.shopping.modules.designer.core.view;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 组件视图(决定组件的显示)
 * @author majian
 */
public abstract class ComponentView {

    /**
     * 不使用lombok生成setter、setter
     */

    private String type; //组件类型

    private String title; //标题
    private String backgroundColor; //背景色

    private String width; //宽度
    private String height;  //高度

    private String widthUnit; //宽度单位
    private String heightUnit;  //高度单位

    //坐标
    private String x;
    private String y;

    //坐标单位
    private String xUnit;

    private String yUnit;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(String widthUnit) {
        this.widthUnit = widthUnit;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getxUnit() {
        return xUnit;
    }

    public void setxUnit(String xUnit) {
        this.xUnit = xUnit;
    }

    public String getyUnit() {
        return yUnit;
    }

    public void setyUnit(String yUnit) {
        this.yUnit = yUnit;
    }
}
