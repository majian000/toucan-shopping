package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.seller.entity.SellerShopCategory;
import com.toucan.shopping.modules.seller.mapper.SellerShopCategoryMapper;
import com.toucan.shopping.modules.seller.service.SellerShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户店铺类别服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SellerShopCategoryServiceImpl implements SellerShopCategoryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerShopCategoryMapper sllerShopCategoryMapper;



    @Override
    public int save(SellerShopCategory entity) {
        return sllerShopCategoryMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopCategoryMapper.deleteById(id);
    }

    @Override
    public int update(SellerShopCategory entity) {
        return sllerShopCategoryMapper.update(entity);
    }

    @Override
    public List<SellerShopCategory> findListByEntity(SellerShopCategory query) {
        return sllerShopCategoryMapper.findListByEntity(query);
    }

}
