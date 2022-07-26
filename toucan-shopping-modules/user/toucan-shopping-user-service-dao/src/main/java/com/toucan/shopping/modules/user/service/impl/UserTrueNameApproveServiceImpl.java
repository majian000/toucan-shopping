package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserApp;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.mapper.UserAppMapper;
import com.toucan.shopping.modules.user.mapper.UserTrueNameApproveMapper;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import com.toucan.shopping.modules.user.service.UserAppService;
import com.toucan.shopping.modules.user.service.UserTrueNameApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTrueNameApproveServiceImpl implements UserTrueNameApproveService {

    @Autowired
    private UserTrueNameApproveMapper userTrueNameApproveMapper;


    @Override
    public int save(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.insert(entity);
    }

    @Override
    public int update(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.update(entity);
    }


    @Override
    public List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.findListByEntity(entity);
    }

    @Override
    public List<UserTrueNameApprove> findListByEntityOrderByUpdateDateDesc(UserTrueNameApprove entity) {
        return userTrueNameApproveMapper.findListByEntityOrderByUpdateDateDesc(entity);
    }


    @Override
    public PageInfo<UserTrueNameApprove> queryListPage(UserTrueNameApprovePageInfo queryPageInfo) {
        PageInfo<UserTrueNameApprove> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userTrueNameApproveMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(userTrueNameApproveMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return userTrueNameApproveMapper.deleteById(id);
    }
}
