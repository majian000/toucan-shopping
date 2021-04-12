package com.toucan.shopping.modules.area.vo;

import lombok.Data;

import java.util.Date;

/**
 * 轮播图地区关联
 *
 * @author majian
 */
@Data
public class BannerAreaVO {
    private Long id; //主键
    private Long bannerId; //轮播图
    private String areaCode; //地区编码
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


    private String[] areaCodeArray; //地区编码列表

}
