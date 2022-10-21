package com.toucan.shopping.modules.seller.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class FreightTemplatePageInfo extends PageInfo<FreightTemplate> {


    // ===============查询条件===================
    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 店铺ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    /**
     * 所属用户ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;


    //==============================================





}
