package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductApproveImgService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ShopProductApproveImgServiceImpl implements ShopProductApproveImgService {

    @Autowired
    private ShopProductApproveImgMapper shopProductImgMapper;

    @Override
    public List<ShopProductApproveImg> queryAllList(ShopProductApproveImg queryModel) {
        return shopProductImgMapper.queryAllList(queryModel);
    }

    @Override
    public List<ShopProductApproveImg> queryList(ShopProductApproveImgVO queryModel) {
        return shopProductImgMapper.queryList(queryModel);
    }

    @Override
    public List<ShopProductApproveImg> queryListOrderByCreateDateAsc(ShopProductApproveImgVO queryModel) {
        return shopProductImgMapper.queryListOrderByCreateDateAsc(queryModel);
    }

    @Override
    public List<ShopProductApproveImg> queryListOrderByImgSortAsc(ShopProductApproveImgVO queryModel) {
        return shopProductImgMapper.queryListOrderByImgSortAsc(queryModel);
    }

    @Override
    public int saves(List<ShopProductApproveImg> entitys) {
        return shopProductImgMapper.inserts(entitys);
    }

    @Override
    public List<ShopProductApproveImg> queryByIdList(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids))
        {
            return null;
        }
        return shopProductImgMapper.queryByIdList(ids);
    }

    @Override
    public int deleteByProductApproveId(Long productApproveId) {
        return shopProductImgMapper.deleteByProductApproveId(productApproveId);
    }


    @Override
    public int deleteByIdList(List<Long> idList) {
        return shopProductImgMapper.deleteByIdList(idList);
    }

    @Override
    public int updateResumeByShopProductApproveId(Long productApproveId) {
        return shopProductImgMapper.updateResumeByShopProductApproveId(productApproveId);
    }

    @Override
    public List<ShopProductApproveImg> queryListByApproveId(Long approveId) {
        return shopProductImgMapper.queryListByApproveId(approveId);
    }
}
