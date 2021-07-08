package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.UserApp;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.mapper.UserAppMapper;
import com.toucan.shopping.modules.user.mapper.UserTrueNameApproveMapper;
import com.toucan.shopping.modules.user.service.UserAppService;
import com.toucan.shopping.modules.user.service.UserTrueNameApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTrueNameApproveServiceImpl implements UserTrueNameApproveService {

    @Autowired
    private UserTrueNameApproveMapper userTrueNameApproveMapper;


    @Override
    public int save(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.insert(entity);
    }


    @Override
    public List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.findListByEntity(entity);
    }
}
