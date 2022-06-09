package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 店铺商品介绍 VO
 *
 * @author majian
 * @date 2021-8-3 09:23:31
 */
@Data
public class ShopProductDescriptionVO extends ShopProductDescription {


    /**
     * 商品介绍的图片列表
     */
    List<ShopProductDescriptionImgVO> productDescriptionImgs;


}
