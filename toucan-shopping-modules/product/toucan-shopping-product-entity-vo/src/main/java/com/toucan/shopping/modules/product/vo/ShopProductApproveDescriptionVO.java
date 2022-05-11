package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import lombok.Data;

import java.util.List;

/**
 * 店铺商品审核的商品介绍 VO
 *
 * @author majian
 * @date 2021-8-3 09:23:31
 */
@Data
public class ShopProductApproveDescriptionVO extends ShopProductApproveDescription {

    /**
     * 商品介绍的图片列表
     */
    List<ShopProductApproveDescriptionImgVO> productDescriptionImgs;



}
