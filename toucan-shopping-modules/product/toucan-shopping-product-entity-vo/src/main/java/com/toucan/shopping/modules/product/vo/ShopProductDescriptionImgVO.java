package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.entity.ShopProductDescriptionImg;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 店铺商品的商品介绍图片 VO
 *
 * @author majian
 * @date 2022-5-17 14:10:29
 */
@Data
public class ShopProductDescriptionImgVO extends ShopProductDescriptionImg {

    private List<Long> shopProductIdList; //店铺商品审核ID列表

    private String httpFilePath; //HTTP访问地址

    /**
     * 图片
     */
    @JSONField(serialize = false)
    private MultipartFile imgFile;



}
