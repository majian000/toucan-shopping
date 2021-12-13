package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserHeadSculptureApproveRecord;

import java.util.List;

/**
 * 用户实名审核
 */
public interface UserHeadSculptureApproveRecordService {

    int save(UserHeadSculptureApproveRecord entity);

    List<UserHeadSculptureApproveRecord> findListByEntity(UserHeadSculptureApproveRecord entity);
}
