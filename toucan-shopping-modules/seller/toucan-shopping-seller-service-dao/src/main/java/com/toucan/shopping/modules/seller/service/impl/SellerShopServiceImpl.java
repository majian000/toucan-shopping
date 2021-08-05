package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.mapper.SellerShopMapper;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SellerShopServiceImpl implements SellerShopService {


    @Autowired
    private SellerShopMapper sllerShopMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SellerShop entity) {
        return sllerShopMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopMapper.deleteById(id);
    }

    @Override
    public int update(SellerShop entity) {
        return sllerShopMapper.update(entity);
    }

    @Override
    public List<SellerShop> findListByEntity(SellerShop query) {
        return sllerShopMapper.findListByEntity(query);
    }
}
