package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserDetail;

import java.util.List;

public interface UserDetailService {

    List<UserDetail> findListByEntity(UserDetail userDetail);

    int save(UserDetail entity);

    int update(UserDetail entity);

    int updateInfo(UserDetail entity);

    int deleteById(Long id);

    List<UserDetail> findById(Long id);

    List<UserDetail> findByUserMainId(Long id);

    List<UserDetail> queryListByUserId(Long[] userIdArray);
}
