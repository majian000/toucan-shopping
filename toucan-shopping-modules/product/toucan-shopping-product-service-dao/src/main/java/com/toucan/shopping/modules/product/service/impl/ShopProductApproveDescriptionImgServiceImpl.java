package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveDescriptionImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductApproveDescriptionImgService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveDescriptionImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductApproveDescriptionImgServiceImpl implements ShopProductApproveDescriptionImgService {

    @Autowired
    private ShopProductApproveDescriptionImgMapper shopProductApproveDescriptionMapper;

    @Override
    public int saves(List<ShopProductApproveDescriptionImg> entitys) {
        return shopProductApproveDescriptionMapper.inserts(entitys);
    }

    @Override
    public int deleteByProductApproveId(Long productApproveId) {
        return shopProductApproveDescriptionMapper.deleteByProductApproveId(productApproveId);
    }

    @Override
    public List<ShopProductApproveDescriptionImgVO> queryVOListByProductApproveIdAndDescriptionIdOrderBySortDesc(Long productApproveId, Long descriptionId) {
        return shopProductApproveDescriptionMapper.queryVOListByProductApproveIdAndDescriptionIdOrderBySortDesc(productApproveId,descriptionId);
    }


    @Override
    public int updateResumeByIdList(List<Long> idList) {
        return shopProductApproveDescriptionMapper.updateResumeByIdList(idList);
    }
}
