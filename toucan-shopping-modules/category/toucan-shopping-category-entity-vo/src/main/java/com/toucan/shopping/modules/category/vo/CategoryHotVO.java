package com.toucan.shopping.modules.category.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.entity.CategoryHot;
import com.toucan.shopping.modules.category.entity.CategoryImg;
import lombok.Data;

import java.util.List;

/**
 * 热门商品类别
 *
 * @author majian
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryHotVO extends CategoryHot {


    /**
     * 类别名称
     */
    private String name;


    /**
     * 上级节点名称
     */
    private String parentName;


    private List<CategoryHotTreeVO> children;

    private List<CategoryImg> categoryImgs;

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
     * PC端首页扩展样式
     */
    private String pcIndexStyle;

    /**
     * 节点路径,例如:家用电器/电视/全面屏电视
     */
    private String namePath;

}
