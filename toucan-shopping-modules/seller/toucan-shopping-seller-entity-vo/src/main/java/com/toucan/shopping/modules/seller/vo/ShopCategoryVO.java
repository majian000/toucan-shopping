package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.ShopCategory;
import lombok.Data;

import java.util.List;

/**
 * 店铺类别
 *
 * @author majian
 */
@Data
public class ShopCategoryVO extends ShopCategory {




    /**
     * 上级节点名称
     */
    private String parentName;


    private List<ShopCategoryVO> children;

    /**
     * 上级节点ID指针,随着遍历上级节点而改变
     */
    private Long parentIdPoint;


    /**
     * 创建人
     */
    private String createAdminUsername;

    /**
     * 修改人
     */
    private String updateAdminUsername;

    private Long[] idArray; //ID数组

    /**
     * 所有根节点链接
     */
    private String rootLinks;

    /**
     * 模糊查询名称
     */
    private String nameLike;


    /**
     * 节点路径,例如:家用电器/电视/全面屏电视
     */
    private String namePath;
}
