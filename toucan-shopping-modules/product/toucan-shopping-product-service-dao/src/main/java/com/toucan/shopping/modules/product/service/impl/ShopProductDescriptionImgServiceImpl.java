package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductDescriptionImg;
import com.toucan.shopping.modules.product.mapper.ShopProductDescriptionImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductDescriptionImgService;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductDescriptionImgServiceImpl implements ShopProductDescriptionImgService {

    @Autowired
    private ShopProductDescriptionImgMapper shopProductDescriptionImgMapper;

    @Override
    public int saves(List<ShopProductDescriptionImg> entitys) {
        return shopProductDescriptionImgMapper.inserts(entitys);
    }

    @Override
    public int deleteByShopProductId(Long shopProductId) {
        return shopProductDescriptionImgMapper.deleteByShopProductId(shopProductId);
    }

    @Override
    public List<ShopProductDescriptionImgVO> queryVOListByProductIdAndDescriptionIdOrderBySortDesc(Long productId, Long descriptionId) {
        return shopProductDescriptionImgMapper.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(productId,descriptionId);
    }

    @Override
    public int updateResumeByIdList(List<Long> idList) {
        return shopProductDescriptionImgMapper.updateResumeByIdList(idList);
    }
}
