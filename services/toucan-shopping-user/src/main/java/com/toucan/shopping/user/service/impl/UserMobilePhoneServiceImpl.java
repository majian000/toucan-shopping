package com.toucan.shopping.user.service.impl;

import com.toucan.shopping.user.entity.UserMobilePhone;
import com.toucan.shopping.user.mapper.UserMobilePhoneMapper;
import com.toucan.shopping.user.service.UserMobilePhoneService;
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
    public List<UserMobilePhone> queryListByUserId(Long[] userIdArray) {
        return userMobilePhoneMapper.queryListByUserId(userIdArray);
    }
}
