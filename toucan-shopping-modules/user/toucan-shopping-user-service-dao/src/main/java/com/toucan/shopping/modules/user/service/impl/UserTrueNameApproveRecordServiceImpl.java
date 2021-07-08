package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.entity.UserTrueNameApproveRecord;
import com.toucan.shopping.modules.user.mapper.UserTrueNameApproveRecordMapper;
import com.toucan.shopping.modules.user.service.UserTrueNameApproveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTrueNameApproveRecordServiceImpl implements UserTrueNameApproveRecordService {

    @Autowired
    private UserTrueNameApproveRecordMapper userTrueNameApproveRecordMapper;


    @Override
    public int save(UserTrueNameApproveRecord entity) {
        return userTrueNameApproveRecordMapper.insert(entity);
    }


    @Override
    public List<UserTrueNameApproveRecord> findListByEntity(UserTrueNameApproveRecord entity) {
        return userTrueNameApproveRecordMapper.findListByEntity(entity);
    }
}
