package com.toucan.shopping.center.user.service.impl;

import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.entity.UserDetail;
import com.toucan.shopping.center.user.mapper.UserDetailMapper;
import com.toucan.shopping.center.user.mapper.UserMapper;
import com.toucan.shopping.center.user.service.UserDetailService;
import com.toucan.shopping.center.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.owl.jdbc.aop.annotation.DataSourceSelector;
//import com.owl.jdbc.enums.DataSourceType;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public List<UserDetail> findListByEntity(UserDetail entity) {
        return userDetailMapper.findListByEntity(entity);
    }


    @Override
    @Transactional
//    @DataSourceSelector
    public int save(UserDetail entity) {
        return userDetailMapper.insert(entity);
    }

    @Override
    public int update(UserDetail entity) {
        return userDetailMapper.update(entity);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return userDetailMapper.deleteById(id);
    }

    @Override
    public List<UserDetail> findById(Long id) {
        return userDetailMapper.findById(id);
    }

    @Override
    public List<UserDetail> queryListByUserId(Long[] userIdArray) {
        return userDetailMapper.queryListByUserId(userIdArray);
    }

}
