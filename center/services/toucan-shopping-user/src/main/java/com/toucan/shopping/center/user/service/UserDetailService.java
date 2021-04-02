package com.toucan.shopping.center.user.service;


import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.entity.UserDetail;

import java.util.List;

public interface UserDetailService {

    List<UserDetail> findListByEntity(UserDetail userDetail);

    int save(UserDetail entity);

    int update(UserDetail entity);

    int deleteById(Long id);

    List<UserDetail> findById(Long id);


    List<UserDetail> queryListByUserId(Long[] userIdArray);
}
