package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.mapper.UserBuyCarItemMapper;
import com.toucan.shopping.modules.user.service.UserBuyCarItemService;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
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
public class UserBuyCarItemServiceImpl implements UserBuyCarItemService {

    @Autowired
    private UserBuyCarItemMapper userBuyCarItemMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public List<UserBuyCarItem> findListByEntity(UserBuyCarItemVO query) {
        return userBuyCarItemMapper.findListByEntity(query);
    }

    @Override
    public List<UserBuyCarItem> findListByUserMainId(Long userMainId) {
        return userBuyCarItemMapper.findListByUserMainId(userMainId);
    }

    @Override
    public UserBuyCarItem findById(Long id) {
        return userBuyCarItemMapper.findById(id);
    }

    @Override
    public int save(UserBuyCarItem entity) {
        return userBuyCarItemMapper.insert(entity);
    }

    @Override
    public int update(UserBuyCarItem entity) {
        return userBuyCarItemMapper.update(entity);
    }

    @Override
    public int deleteById(Long id) {
        return userBuyCarItemMapper.deleteById(id);
    }

    @Override
    public int deleteByIdAndUserMainId(Long id, Long userMainId) {
        return userBuyCarItemMapper.deleteByIdAndUserMainId(id,userMainId);
    }

    @Override
    public int deleteByUserMainId(Long userMainId) {
        return userBuyCarItemMapper.deleteByUserMainId(userMainId);
    }
}
