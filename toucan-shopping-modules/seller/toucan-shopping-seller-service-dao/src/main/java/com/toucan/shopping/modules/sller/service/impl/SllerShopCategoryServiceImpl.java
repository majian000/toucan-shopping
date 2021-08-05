package com.toucan.shopping.modules.sller.service.impl;

import com.toucan.shopping.modules.sller.entity.SllerShopCategory;
import com.toucan.shopping.modules.sller.mapper.SllerShopCategoryMapper;
import com.toucan.shopping.modules.sller.service.SllerShopCategoryService;
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
public class SllerShopCategoryServiceImpl implements SllerShopCategoryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SllerShopCategoryMapper sllerShopCategoryMapper;



    @Override
    public int save(SllerShopCategory entity) {
        return sllerShopCategoryMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopCategoryMapper.deleteById(id);
    }

    @Override
    public int update(SllerShopCategory entity) {
        return sllerShopCategoryMapper.update(entity);
    }

    @Override
    public List<SllerShopCategory> findListByEntity(SllerShopCategory query) {
        return sllerShopCategoryMapper.findListByEntity(query);
    }

}
