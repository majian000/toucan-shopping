package com.toucan.shopping.category.entity;

import lombok.Data;

import java.util.Date;

/**
 * 类别与地区关联
 *
 * @author majian
 */
@Data
public class CategoryArea {
    private Long id; //主键
    private Long categoryId; //类别ID
    private String areaCode; //地区编码
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
