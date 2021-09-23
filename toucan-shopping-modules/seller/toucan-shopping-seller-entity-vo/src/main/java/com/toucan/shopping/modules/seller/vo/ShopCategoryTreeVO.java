package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.common.vo.bootstrap.State;
import lombok.Data;

import java.util.List;

/**
 * 店铺类别
 */
@Data
public class ShopCategoryTreeVO extends ShopCategoryVO {


    /**
     * 节点标题
     */
    private String title;

    /**
     * 节点名
     */
    private String text;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 子节点
     */
    private List<ShopCategoryTreeVO> nodes;



    /**
     * 节点属性
     */
    private State state = new State();



    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

}
