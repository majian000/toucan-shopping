package com.toucan.shopping.center.user.service.impl;

import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.entity.UserEmail;
import com.toucan.shopping.center.user.mapper.UserEmailMapper;
import com.toucan.shopping.center.user.mapper.UserMapper;
import com.toucan.shopping.center.user.service.UserEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserEmailServiceImpl implements UserEmailService {

    @Autowired
    private UserEmailMapper userEmailMapper;

    @Override
    public List<UserEmail> findListByEntity(UserEmail entity) {
        return userEmailMapper.findListByEntity(entity);
    }


    @Override
    @Transactional
//    @DataSourceSelector
    public int save(UserEmail entity) {
        return userEmailMapper.insert(entity);
    }

}
