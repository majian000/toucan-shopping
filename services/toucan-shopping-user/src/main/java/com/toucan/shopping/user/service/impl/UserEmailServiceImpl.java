package com.toucan.shopping.user.service.impl;

import com.toucan.shopping.user.entity.UserEmail;
import com.toucan.shopping.user.mapper.UserEmailMapper;
import com.toucan.shopping.user.service.UserEmailService;
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
