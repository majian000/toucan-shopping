package com.toucan.shopping.center.user.service.impl;

import com.toucan.shopping.center.user.entity.UserEmail;
import com.toucan.shopping.center.user.entity.UserMobilePhone;
import com.toucan.shopping.center.user.mapper.UserEmailMapper;
import com.toucan.shopping.center.user.mapper.UserMobilePhoneMapper;
import com.toucan.shopping.center.user.service.UserEmailService;
import com.toucan.shopping.center.user.service.UserMobilePhoneService;
import org.apache.commons.collections.CollectionUtils;
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

    @Transactional
    @Override
    public int save(UserMobilePhone entity) {
        return userMobilePhoneMapper.insert(entity);
    }

    @Override
    public List<UserMobilePhone> queryListByUserId(Long[] userIdArray) {
        return userMobilePhoneMapper.queryListByUserId(userIdArray);
    }
}
