package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.mapper.ShopProductImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductImgService;
import com.toucan.shopping.modules.product.vo.ShopProductImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ShopProductImgServiceImpl implements ShopProductImgService {

    @Autowired
    private ShopProductImgMapper shopProductImgMapper;

    @Override
    public List<ShopProductImg> queryAllList(ShopProductImg queryModel) {
        return shopProductImgMapper.queryAllList(queryModel);
    }

    @Override
    public List<ShopProductImg> queryList(ShopProductImgVO queryModel) {
        return shopProductImgMapper.queryList(queryModel);
    }

    @Override
    public int saves(List<ShopProductImg> entitys) {
        return shopProductImgMapper.inserts(entitys);
    }

    @Override
    public List<ShopProductImg> queryByIdList(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids))
        {
            return null;
        }
        return shopProductImgMapper.queryByIdList(ids);
    }
}
