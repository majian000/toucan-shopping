package com.toucan.shopping.category.export.entity;

import lombok.Data;

import java.util.Date;

/**
 * 商品类别
 *
 * @author majian
 */
@Data
public class Category {
    private Long id; //主键
    private Long parentId; //上级类别
    private String name; //类别名称
    private Long categorySort; //排序
    private String createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID

}
