package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.mapper.UserUserNameMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.service.UserUserNameService;
import com.toucan.shopping.modules.user.entity.UserUserName;
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

    @Override
    public List<UserUserName> findListByEntityNothingDeleteStatus(UserUserName entity) {
        return userUserNameMapper.findListByEntityNothingDeleteStatus(entity);
    }

    @Override
    public List<UserUserName> queryListByUserId(Long[] userIdArray) {
        return userUserNameMapper.queryListByUserId(userIdArray);
    }

    @Override
    public List<UserUserName> findListByUserMainId(Long userMainId) {
        return userUserNameMapper.findListByUserMainId(userMainId);
    }

    @Override
    public List<UserUserName> findListByUserName(String username) {
        return userUserNameMapper.findListByUserName(username);
    }

    @Override
    public List<UserUserName> findListByUserName(String username,List<Long> userMianIdList) {
        return userUserNameMapper.findListByUserNameAndUserMainIdList(username,userMianIdList);
    }

    @Override
    public PageInfo<UserUserName> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo) {
        PageInfo<UserUserName> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userUserNameMapper.queryListPageNothingDeleteStatus(queryPageInfo));
        pageInfo.setTotal(userUserNameMapper.queryListPageNothingDeleteStatusCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int updateDeleteStatus(Short deleteStatus, Long userMainId, String username) {
        return userUserNameMapper.updateDeleteStatus(deleteStatus,userMainId,username);
    }

    @Override
    public int deleteByUserMainId(Long userMainId) {
        return userUserNameMapper.deleteByUserMainId(userMainId);
    }


}
