package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设计器页面
 *
 * @author majian
 */
@Data
public class SellerDesignerPage {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 关联店铺
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    private String pageJson; //页面json
    private String type; //类型 1:预览页 2:正式页
    private String position; //位置 1:PC首页
    private String designerVersion; //设计器版本

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //创建时间
    private String createrId; //创建人ID
    private String updaterId; //修改人ID ADMIN_开头是管理员


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间


    private String appCode; //所属应用
    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;




}
