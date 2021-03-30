package com.toucan.shopping.center.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.mapper.UserMapper;
import com.toucan.shopping.center.user.page.UserPageInfo;
import com.toucan.shopping.center.user.service.UserService;
//import com.owl.jdbc.aop.annotation.DataSourceSelector;
//import com.owl.jdbc.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findListByEntity(User user) {
        return userMapper.findListByEntity(user);
    }


    @Override
    @Transactional
//    @DataSourceSelector
    public int save(User user) {
        return userMapper.insert(user);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public List<User> findById(Long id) {
        return userMapper.findById(id);
    }


    //    @DataSourceSelector
    @Override
    public int addUserToAppCode(String mobile, String appCode) {
        return 0;
    }

    @Override
    public PageInfo<User> queryListPage(UserPageInfo appPageInfo) {
        PageHelper.startPage(appPageInfo.getPage(), appPageInfo.getSize());
        List<User> apps = userMapper.queryListPage(appPageInfo);
        PageInfo<User> pageInfo = new PageInfo<>(apps);
        return pageInfo;
    }
}
