package com.toucan.shopping.modules.search.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品搜索
 */
@Data
public class ProductSearchVO extends SearchVO{

    private String keyword; //查询关键字

    private BigDecimal price; //价格

    private String cid; //分类ID

    private List<Long> brandIds; //品牌ID

    private String bid; //品牌ID

    private String ebids; //将这些品牌ID排除出查询条件

    private String qbs="t"; //是否先查询品牌 f:不查询 t:查询

    private String productName; //商品名称

    private String bn; //品牌名称

    private String categoryName; //分类名称

    private String ab; //属性集合字符串 多个用,分割

    private String abids; //属性ID列表 多个用,分割

    private String ps; //单价开始区间

    private String pe; //单价结束区间

    private Double psd; //单价开始区间

    private Double ped; //单价结束区间

    private String pst; //价格排序 null:默认没排序 desc:从高到低 asc:从低到高

    private String pdst; //时间排序 null:默认没排序 desc:发布时间降序 asc:发布时间升序


    private List<ProductSearchAttributeVO> searchAttributes; //属性集合

}
