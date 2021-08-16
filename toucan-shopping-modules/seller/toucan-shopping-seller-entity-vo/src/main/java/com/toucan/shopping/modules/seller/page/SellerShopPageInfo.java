package com.toucan.shopping.modules.seller.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SellerShopPageInfo extends PageInfo<SellerShop> {


    // ===============查询条件===================
    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 所属用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 公开店铺ID
     */
    private String publicShopId;

    /**
     * 店铺名称
     */
    private String name;


    /**
     * 审核状态 1审核中 2审核通过 3审核驳回
     */
    private Integer approveStatus;

    /**
     * 店铺类型 1个人 2企业
     */
    private Integer type;




    //==============================================





}
