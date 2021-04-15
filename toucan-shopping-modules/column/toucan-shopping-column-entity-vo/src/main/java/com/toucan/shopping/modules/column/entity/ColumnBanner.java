package com.toucan.shopping.modules.column.entity;

import lombok.Data;

import java.util.Date;

/**
 * 栏目轮播图关联
 *
 * @author majian
 */
@Data
public class ColumnBanner {
    private Long id; //主键
    private Long bannerId; //轮播图主键
    private Long columnId; //栏目主键
    private Integer position; //位置 1:栏目左侧
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private String appCode; //所属应用
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
