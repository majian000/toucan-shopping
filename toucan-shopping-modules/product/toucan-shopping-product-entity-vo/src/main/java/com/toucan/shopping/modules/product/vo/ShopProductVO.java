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

    private String categoryName; //类别名称

    private String categoryPath; //类别路径

    /**
     * 主图文件路径
     */
    private String mainPhotoFilePath;

    /**
     * 验证码
     */
    private String vcode;

}
