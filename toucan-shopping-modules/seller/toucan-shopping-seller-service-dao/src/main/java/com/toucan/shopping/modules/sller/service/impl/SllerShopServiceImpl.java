package com.toucan.shopping.modules.sller.service.impl;

import com.toucan.shopping.modules.sller.entity.SllerShop;
import com.toucan.shopping.modules.sller.mapper.SllerShopMapper;
import com.toucan.shopping.modules.sller.service.SllerShopService;
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
public class SllerShopServiceImpl implements SllerShopService {


    @Autowired
    private SllerShopMapper sllerShopMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SllerShop entity) {
        return sllerShopMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopMapper.deleteById(id);
    }

    @Override
    public int update(SllerShop entity) {
        return sllerShopMapper.update(entity);
    }

    @Override
    public List<SllerShop> findListByEntity(SllerShop query) {
        return sllerShopMapper.findListByEntity(query);
    }
}
