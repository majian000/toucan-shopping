package com.toucan.shopping.modules.designer.core.vo.component;


import lombok.Data;

/**
 * 抽象组件
 * 所有组件都继承
 * @author majian
 */
@Data
public abstract class AbstractComponent implements IComponent {

    //坐标
    private long x;
    private long y;



}
