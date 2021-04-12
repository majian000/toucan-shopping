package com.toucan.shopping.modules.column.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 栏目VO
 *
 * @author majian
 */
@Data
public class ColumnVO {
    private Long id; //主键
    private String title; //编码
    private Integer type; //类型 1:pc端 2:移动端
    private Integer showStatus; //显示状态 0隐藏 1显示
    private Integer position; //栏目位置 1首页
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
