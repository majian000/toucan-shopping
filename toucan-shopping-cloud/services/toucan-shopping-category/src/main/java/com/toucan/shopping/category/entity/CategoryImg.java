package com.toucan.shopping.category.entity;

import lombok.Data;

import java.util.Date;

/**
 * 商品类别图片
 *
 * @author majian
 */
@Data
public class CategoryImg {
    private Long id; //主键
    private Long categoryId; //所属类别
    private String src; //图片路径
    private Integer type; //类型 1:pc端 2:移动端
    private Long categorySort; //排序
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
