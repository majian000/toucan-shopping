package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;

import java.util.List;

/**
 * 用户实名审核
 */
public interface UserTrueNameApproveService {

    int save(UserTrueNameApprove entity);

    List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity);
}
