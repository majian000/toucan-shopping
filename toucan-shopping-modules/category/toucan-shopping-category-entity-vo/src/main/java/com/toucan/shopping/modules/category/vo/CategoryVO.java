package com.toucan.shopping.modules.category.vo;

import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.entity.CategoryImg;
import lombok.Data;

import java.util.List;

/**
 * 商品类别
 *
 * @author majian
 */
@Data
public class CategoryVO extends Category {


    private String areaCode; //地区编码


    /**
     * 上级节点名称
     */
    private String parentName;


    private List<CategoryVO> children;

    private List<CategoryImg> categoryImgs;


    /**
     * 创建人
     */
    private String createAdminUsername;

    /**
     * 修改人
     */
    private String updateAdminUsername;

    private Long[] idArray; //ID数组
}
