package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.mapper.UserMobilePhoneMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.service.UserMobilePhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserMobilePhoneServiceImpl implements UserMobilePhoneService {

    @Autowired
    private UserMobilePhoneMapper userMobilePhoneMapper;


    @Override
    public List<UserMobilePhone> findListByEntity(UserMobilePhone entity) {
        return userMobilePhoneMapper.findListByEntity(entity);
    }

    @Override
    public List<UserMobilePhone> findListByMobilePhone(String mobilePhone) {
        return userMobilePhoneMapper.findListByMobilePhone(mobilePhone);
    }

    @Override
    public List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone) {
        return userMobilePhoneMapper.findListByMobilePhoneLike(mobilePhone);
    }

    @Transactional
    @Override
    public int save(UserMobilePhone entity) {
        return userMobilePhoneMapper.insert(entity);
    }

    @Override
    public List<UserMobilePhone> queryListByUserMainId(Long[] userMainIdArray) {
        return userMobilePhoneMapper.queryListByUserMainId(userMainIdArray);
    }

    @Override
    public List<UserMobilePhone> queryListByUserMainIdNothingDeleteStatus(Long[] userMainIdArray) {
        return userMobilePhoneMapper.queryListByUserMainIdNothingDeleteStatus(userMainIdArray);
    }

    @Override
    public PageInfo<UserMobilePhone> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo) {
        PageInfo<UserMobilePhone> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userMobilePhoneMapper.queryListPageNothingDeleteStatus(queryPageInfo));
        pageInfo.setTotal(userMobilePhoneMapper.queryListPageNothingDeleteStatusCount(queryPageInfo));
        return pageInfo;
    }
}
