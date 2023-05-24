package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.mapper.UserEmailMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
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
    public List<UserEmail> queryListByUserId(Long[] userIdArray) {
        return userEmailMapper.queryListByUserId(userIdArray);
    }


    @Override
    @Transactional
//    @DataSourceSelector
    public int save(UserEmail entity) {
        return userEmailMapper.insert(entity);
    }

    @Override
    public List<UserEmail> findListByEntityNothingDeleteStatus(UserEmail entity) {
        return userEmailMapper.findListByEntityNothingDeleteStatus(entity);
    }

    @Override
    public int updateDeleteStatus(Short deleteStatus, Long userMainId, String email) {
        return userEmailMapper.updateDeleteStatus(deleteStatus,userMainId,email);
    }
    @Override
    public int updateDeleteStatusById(Short deleteStatus, Long userMainId, String email,Long id) {
        return userEmailMapper.updateDeleteStatusById(deleteStatus,userMainId,email,id);
    }

    @Override
    public List<UserEmail> findListByEmail(String email) {
        return userEmailMapper.findListByEmail(email);
    }

    @Override
    public List<UserEmail> findListByEmail(String email,List<Long> userMianIdList) {
        return userEmailMapper.findListByEmailAndUserMianIdList(email,userMianIdList);
    }

    @Override
    public List<UserEmail> findListByUserMainId(Long userMainId) {
        return userEmailMapper.findListByUserMainId(userMainId);
    }

    @Override
    public PageInfo<UserEmail> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo) {
        PageInfo<UserEmail> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userEmailMapper.queryListPageNothingDeleteStatus(queryPageInfo));
        pageInfo.setTotal(userEmailMapper.queryListPageNothingDeleteStatusCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteByUserMainId(Long userMainId) {
        return userEmailMapper.deleteByUserMainId(userMainId);
    }

    @Override
    public UserEmail findByUserMainId(Long userMainId) {
        return userEmailMapper.findByUserMainId(userMainId);
    }

}
