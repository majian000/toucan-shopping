package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.entity.ShopProductImg;
import lombok.Data;

import java.util.List;

/**
 * 店铺维度的商品审核与商品预览图关联
 *
 * @author majian
 */
@Data
public class ShopProductApproveImgVO extends ShopProductApproveImg {


    private List<Long> productApproveIdList; //店铺商品ID列表


}
