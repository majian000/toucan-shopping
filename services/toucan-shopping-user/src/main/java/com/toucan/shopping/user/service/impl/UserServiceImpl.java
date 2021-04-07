package com.toucan.shopping.user.service.impl;

import com.toucan.shopping.common.page.PageInfo;
import com.toucan.shopping.user.entity.User;
import com.toucan.shopping.user.mapper.UserMapper;
import com.toucan.shopping.user.page.UserPageInfo;
import com.toucan.shopping.user.service.UserService;
//import com.owl.jdbc.aop.annotation.DataSourceSelector;
//import com.owl.jdbc.enums.DataSourceType;
import com.toucan.shopping.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageInfo<UserVO> queryListPage(UserPageInfo appPageInfo) {
        List<UserVO> users = userMapper.queryListPage(appPageInfo);
        PageInfo<UserVO> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }
}
