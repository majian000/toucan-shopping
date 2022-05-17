package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.vo.ShopProductApproveDescriptionImgVO;

import java.util.List;

public interface ShopProductApproveDescriptionImgService {

    int saves(List<ShopProductApproveDescriptionImg> entitys);

    int deleteByProductApproveId(Long productApproveId);

    /**
     * 根据商品审核ID和商品介绍ID查询
     * @param productApproveId
     * @param descriptionId
     * @return
     */
    List<ShopProductApproveDescriptionImgVO> queryVOListByProductApproveIdAndDescriptionIdOrderBySortDesc(Long productApproveId, Long descriptionId);

    int updateResumeByIdList(List<Long> idList);
}
