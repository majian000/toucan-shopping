package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProduct;
import lombok.Data;

/**
 * 店铺维度的SPU (关联到平台维度的SPU)
 *
 * @author majian
 */
@Data
public class ShopProductVO extends ShopProduct {

    /**
     * 主图文件路径
     */
    private String mainPhotoFilePath;

}
