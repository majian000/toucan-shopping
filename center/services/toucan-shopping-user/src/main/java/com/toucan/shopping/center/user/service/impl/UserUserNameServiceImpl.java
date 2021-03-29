package com.toucan.shopping.center.user.service.impl;

import com.toucan.shopping.center.user.entity.UserMobilePhone;
import com.toucan.shopping.center.user.entity.UserUserName;
import com.toucan.shopping.center.user.mapper.UserMobilePhoneMapper;
import com.toucan.shopping.center.user.mapper.UserUserNameMapper;
import com.toucan.shopping.center.user.service.UserMobilePhoneService;
import com.toucan.shopping.center.user.service.UserUserNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserUserNameServiceImpl implements UserUserNameService {

    @Autowired
    private UserUserNameMapper userUserNameMapper;


    @Override
    public List<UserUserName> findListByEntity(UserUserName entity) {
        return userUserNameMapper.findListByEntity(entity);
    }

    @Transactional
    @Override
    public int save(UserUserName entity) {
        return userUserNameMapper.insert(entity);
    }
}
