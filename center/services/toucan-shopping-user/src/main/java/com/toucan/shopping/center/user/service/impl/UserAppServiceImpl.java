package com.toucan.shopping.center.user.service.impl;

import com.toucan.shopping.center.user.entity.UserApp;
import com.toucan.shopping.center.user.mapper.UserAppMapper;
import com.toucan.shopping.center.user.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAppServiceImpl implements UserAppService {

    @Autowired
    private UserAppMapper userAppMapper;


    @Transactional
    @Override
    public int save(UserApp userApp) {
        return userAppMapper.insert(userApp);
    }


    @Override
    public List<UserApp> findListByEntity(UserApp userApp) {
        return userAppMapper.findListByEntity(userApp);
    }
}
