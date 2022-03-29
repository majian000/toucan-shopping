package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;
import lombok.Data;

/**
 * 店铺商品审核记录
 */
@Data
public class ShopProductApproveRecordVO extends ShopProductApproveRecord {

    private Long shopId; //所属店铺ID


}
