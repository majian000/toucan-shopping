package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.entity.ShopProductDescriptionImg;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveDescriptionImgMapper;
import com.toucan.shopping.modules.product.mapper.ShopProductDescriptionImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductApproveDescriptionImgService;
import com.toucan.shopping.modules.product.service.ShopProductDescriptionImgService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveDescriptionImgVO;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductDescriptionImgServiceImpl implements ShopProductDescriptionImgService {

    @Autowired
    private ShopProductDescriptionImgMapper shopProductDescriptionMapper;

    @Override
    public int saves(List<ShopProductDescriptionImg> entitys) {
        return shopProductDescriptionMapper.inserts(entitys);
    }

    @Override
    public int deleteByShopProductId(Long shopProductId) {
        return shopProductDescriptionMapper.deleteByShopProductId(shopProductId);
    }

    @Override
    public List<ShopProductDescriptionImgVO> queryVOListByProductIdAndDescriptionId(Long productId, Long descriptionId) {
        return shopProductDescriptionMapper.queryVOListByProductIdAndDescriptionId(productId,descriptionId);
    }
}
