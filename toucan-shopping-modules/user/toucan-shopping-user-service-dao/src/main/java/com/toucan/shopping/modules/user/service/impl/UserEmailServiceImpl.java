package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.mapper.UserEmailMapper;
import com.toucan.shopping.modules.user.service.UserEmailService;
import com.toucan.shopping.modules.user.entity.UserEmail;
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
