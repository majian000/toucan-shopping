package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.entity.UserTrueNameApproveRecord;

import java.util.List;

/**
 * 用户实名审核
 */
public interface UserTrueNameApproveRecordService {

    int save(UserTrueNameApproveRecord entity);

    List<UserTrueNameApproveRecord> findListByEntity(UserTrueNameApproveRecord entity);
}
