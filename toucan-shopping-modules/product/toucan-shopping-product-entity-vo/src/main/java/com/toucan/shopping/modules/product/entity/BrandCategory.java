package com.toucan.shopping.modules.product.entity;

import lombok.Data;

import java.util.Date;

/**
 * 品牌分类表
 * @author majian
 */
@Data
public class BrandCategory {
    private Long id; //主键
    private Long brandId; //品牌ID
    private Long categoryId; //分类ID
    private Date createDate; //创建时间
    private Integer deleteStatus; //删除状态 0未删除 1已删除

}
