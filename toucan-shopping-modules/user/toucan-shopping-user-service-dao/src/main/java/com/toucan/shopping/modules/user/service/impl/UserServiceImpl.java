package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.mapper.UserMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.service.UserService;
import com.toucan.shopping.modules.user.vo.UserVO;
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
    public List<User> findListByUserMainId(Long userMainId) {
        return userMapper.findListByUserMainId(userMainId);
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

    @Override
    public int updateEnableStatus(Short enableStatus,Long userMainId) {
        return userMapper.updateEnableStatus(enableStatus,userMainId);
    }


    //    @DataSourceSelector
    @Override
    public int addUserToAppCode(String mobile, String appCode) {
        return 0;
    }

    @Override
    public PageInfo<UserVO> queryListPage(UserPageInfo queryPageInfo) {
        PageInfo<UserVO> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(userMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
