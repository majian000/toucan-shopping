package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 店铺轮播图
 *
 * @author majian
 */
@Data
public class ShopBanner {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 关联店铺
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    private String title; //标题
    private String imgPath; //图片路径
    private String clickPath; //点击路径
    private String position; //位置(多个用,分割) PC_INDEX:PC首页 H5_INDEX:H5首页
    private Integer bannerSort; //排序 从大到小

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startShowDate; //开始展示时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endShowDate; //结束展示时间

    private Short showStatus; //显示状态 1:显示 0:隐藏

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //创建时间
    private String createrId; //创建人ID
    private String updaterId; //修改人ID



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    private String remark; //备注

    private String appCode; //所属应用
    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    private String httpImgPath;
    private String startShowDateString;
    private String endShowDateString;




}
