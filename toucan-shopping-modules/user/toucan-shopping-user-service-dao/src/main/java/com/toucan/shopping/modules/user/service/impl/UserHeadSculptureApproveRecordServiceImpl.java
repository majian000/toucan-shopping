package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.UserHeadSculptureApproveRecord;
import com.toucan.shopping.modules.user.mapper.UserHeadSculptureApproveRecordMapper;
import com.toucan.shopping.modules.user.service.UserHeadSculptureApproveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHeadSculptureApproveRecordServiceImpl implements UserHeadSculptureApproveRecordService {

    @Autowired
    private UserHeadSculptureApproveRecordMapper userHeadSculptureApproveRecordMapper;


    @Override
    public int save(UserHeadSculptureApproveRecord entity) {
        return userHeadSculptureApproveRecordMapper.insert(entity);
    }


    @Override
    public List<UserHeadSculptureApproveRecord> findListByEntity(UserHeadSculptureApproveRecord entity) {
        return userHeadSculptureApproveRecordMapper.findListByEntity(entity);
    }
}
