package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.mapper.ShopProductImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductImgServiceImpl implements ShopProductImgService {

    @Autowired
    private ShopProductImgMapper shopProductSpuImgMapper;

    @Override
    public List<ShopProductImg> queryAllList(ShopProductImg queryModel) {
        return shopProductSpuImgMapper.queryAllList(queryModel);
    }
}