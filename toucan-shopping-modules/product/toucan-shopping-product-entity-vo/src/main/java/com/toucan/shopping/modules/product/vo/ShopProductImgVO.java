package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProductImg;
import lombok.Data;

import java.util.List;

/**
 * 店铺维度的SPU与商品预览图关联
 *
 * @author majian
 */
@Data
public class ShopProductImgVO extends ShopProductImg {


    private List<Long> shopProductIdList; //店铺商品ID列表


}
