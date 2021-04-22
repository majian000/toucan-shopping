package com.toucan.shopping.modules.area.entity;

import lombok.Data;

import java.util.Date;

/**
 * 首页限时特卖
 *
 * @author majian
 */
@Data
public class SecondKillColumn {
    private Long id; //主键
    private String title; //标题
    private Integer columnSort; //排序 从大到小
    private Date updateDate; //创建时间
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID
    private Date createDate; //创建时间
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
