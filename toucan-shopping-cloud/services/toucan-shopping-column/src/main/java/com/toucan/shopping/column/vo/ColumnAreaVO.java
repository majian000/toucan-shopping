package com.toucan.shopping.column.vo;

import lombok.Data;

import java.util.Date;

/**
 * 栏目地区VO
 *
 * @author majian
 */
@Data
public class ColumnAreaVO {
    private Long id; //主键
    private Long columnId; //栏目主键
    private String areaCode; //地区编码
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
