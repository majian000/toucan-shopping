package com.toucan.shopping.user.service.impl;

import com.toucan.shopping.user.entity.UserApp;
import com.toucan.shopping.user.mapper.UserAppMapper;
import com.toucan.shopping.user.service.UserAppService;
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
