package com.toucan.shopping.modules.area.entity;

import lombok.Data;

import java.util.Date;

/**
 * 轮播图
 *
 * @author majian
 */
@Data
public class Banner  {
    private Long id; //主键
    private String title; //标题
    private String imgPath; //图片路径
    private String clickPath; //点击路径
    private Integer position; //类型 0:首页顶部
    private Integer bannerSort; //排序 从大到小
    private Date updateDate; //创建时间
    private String createAdminId; //创建人ID
    private String updateAdminId; //修改人ID


    private Date createDate; //创建时间

    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
