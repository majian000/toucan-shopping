package com.toucan.shopping.column.entity;

import lombok.Data;

import java.util.Date;

/**
 * 栏目图片
 *
 * @author majian
 */
@Data
public class ColumnImg {
    private Long id; //主键
    private Long columnId; //栏目主键
    private String src; //图片地址
    private Integer type; //类型 1:pc端 2:移动端
    private Integer imgSort; //排序 降序
    private String href; //点击跳转路径
    private Integer showStatus; //显示状态 0隐藏 1显示
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
