package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ShopProductDescriptionImg;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;

import java.util.List;

public interface ShopProductDescriptionImgService {

    int saves(List<ShopProductDescriptionImg> entitys);

    int deleteByShopProductId(Long shopProductId);

    /**
     * 根据商品审核ID和商品介绍ID查询
     * @param productId
     * @param descriptionId
     * @return
     */
    List<ShopProductDescriptionImgVO> queryVOListByProductIdAndDescriptionIdOrderBySortDesc(Long productId, Long descriptionId);


    /**
     * 根据商品审核ID和商品介绍ID查询
     * @param productId
     * @param descriptionId
     * @return
     */
    List<ShopProductDescriptionImgVO> queryVOListByProductIdAndDescriptionIdAndTypeOrderBySortDesc(Long productId, Long descriptionId,Integer type);
    /**
     * 根据商品SKU的ID和商品介绍ID查询
     * @param descriptionId
     * @return
     */
    List<ShopProductDescriptionImgVO> queryVOListBySkuIdAndDescriptionIdOrderBySortDesc(Long skuId, Long descriptionId);

    int updateResumeByIdList(List<Long> idList);

}
