package com.toucan.shopping.category.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品类别
 *
 * @author majian
 */
@Data
public class CategoryVO {
    private Long id; //主键
    private Long parentId; //上级类别
    private String name; //类别名称
    private Long categorySort; //排序
    private String createDate; //创建时间
    private Long createUserId; //创建人ID

    private String areaCode; //地区编码
    private Integer type; //类型 1:pc端 2:移动端

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    private List<CategoryVO> children;

    private Long[] idArray; //ID数组

}
