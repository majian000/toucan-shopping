package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.mapper.UserCollectProductMapper;
import com.toucan.shopping.modules.user.service.UserCollectProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户收藏商品
 */
@Service
public class UserCollectProductServiceImpl implements UserCollectProductService {

    @Autowired
    private UserCollectProductMapper userCollectProductMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(UserCollectProduct entity) {
        return userCollectProductMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return userCollectProductMapper.deleteById(id);
    }

}
