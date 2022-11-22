package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateVO;
import lombok.Data;


/**
 * 购物车
 * @author majian
 */
@Data
public class UserBuyCarItemVO extends UserBuyCarItem {


    /**
     * 商品SKU名称
     */
    private String productSkuName;

    /**
     * 商品SKU预览图
     */
    private String httpProductImgPath;

    /**
     * 商品价格
     */
    private Double productPrice;

    /**
     * 属性预览
     */
    private String attributePreview;

    private Double roughWeight; //毛重

    private Double suttle; //净重


    @JsonProperty("isAllowedBuy")
    private boolean isAllowedBuy = true; //是否允许购买

    private Short noAllowedBuyStatus; //不允许购买原因 1:已下架 2:已售罄

    private String noAllowedBuyDesc; //不允许购买描述

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 运费模板ID
     */
    private Long freightTemplateId;

    /**
     * 运费模板
     */
    private UBCIFreightTemplateVO freightTemplateVO;

    private String selectTransportModel; //选择的运送方式

    private Integer lockStockNum = 0; //锁定商品库存数量

}
