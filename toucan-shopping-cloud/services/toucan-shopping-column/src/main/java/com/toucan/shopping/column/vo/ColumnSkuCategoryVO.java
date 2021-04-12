package com.toucan.shopping.column.vo;

import lombok.Data;

import java.util.Date;

/**
 * 栏目VO
 *
 * @author majian
 */
@Data
public class ColumnSkuCategoryVO {
    private Long id; //主键
    private String name; //类别名称
    private String href; //点击类别跳转连接
    private Long columnId; //栏目主键
    private Integer position; //位置 1:栏目顶部 2:栏目左侧
    private Integer categorySort; //排序 降序
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


    private Long[] idArray; //ID数组

}
