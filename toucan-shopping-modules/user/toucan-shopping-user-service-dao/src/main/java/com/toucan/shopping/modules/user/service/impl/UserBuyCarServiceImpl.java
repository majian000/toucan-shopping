package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserBuyCar;
import com.toucan.shopping.modules.user.mapper.ConsigneeAddressMapper;
import com.toucan.shopping.modules.user.mapper.UserBuyCarMapper;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.service.UserBuyCarService;
import com.toucan.shopping.modules.user.vo.UserBuyCarVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户购物车
 * @author majian
 */
@Service
public class UserBuyCarServiceImpl implements UserBuyCarService {

    @Autowired
    private UserBuyCarMapper userBuyCarMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public List<UserBuyCar> findListByEntity(UserBuyCarVO query) {
        return userBuyCarMapper.findListByEntity(query);
    }

    @Override
    public List<UserBuyCar> findListByUserMainId(Long userMainId) {
        return userBuyCarMapper.findListByUserMainId(userMainId);
    }

    @Override
    public int save(UserBuyCar entity) {
        return userBuyCarMapper.insert(entity);
    }

    @Override
    public int update(UserBuyCar entity) {
        return userBuyCarMapper.update(entity);
    }

    @Override
    public int deleteById(Long id) {
        return userBuyCarMapper.deleteById(id);
    }
}
