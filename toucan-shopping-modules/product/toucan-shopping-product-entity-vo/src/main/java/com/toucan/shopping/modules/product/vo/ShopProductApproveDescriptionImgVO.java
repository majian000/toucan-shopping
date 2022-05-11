package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品审核的商品介绍图片 VO
 *
 * @author majian
 * @date 2022-5-11 11:45:22
 */
@Data
public class ShopProductApproveDescriptionImgVO extends ShopProductApproveDescriptionImg {

    private List<Long> productApproveIdList; //店铺商品审核ID列表


    /**
     * 商品主图
     */
    @JSONField(serialize = false)
    private MultipartFile mainPhotoFile;



}
