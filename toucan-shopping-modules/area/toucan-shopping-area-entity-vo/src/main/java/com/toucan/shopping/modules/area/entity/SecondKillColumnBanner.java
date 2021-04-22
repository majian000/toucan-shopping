package com.toucan.shopping.modules.area.entity;

import lombok.Data;

import java.util.Date;

/**
 * 限时特卖栏目与轮播图关联
 *
 * @author majian
 */
@Data
public class SecondKillColumnBanner {
    private Long id; //主键
    private Long secondKillColumnId; //秒杀栏目
    private Long bannerId; //轮播图
    private Date updateDate; //创建时间
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID


    private String appCode; //所属应用

    private Date createDate; //创建时间

    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
