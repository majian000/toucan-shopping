package com.toucan.shopping.modules.user.service;



import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserEmail;
import com.toucan.shopping.modules.user.entity.UserUserName;
import com.toucan.shopping.modules.user.page.UserPageInfo;

import java.util.List;

public interface UserEmailService {

    List<UserEmail> findListByEntity(UserEmail entity);

    List<UserEmail> queryListByUserId(Long[] userIdArray);

    int save(UserEmail entity);


    /**
     * 根据邮箱查询关联
     * @param email
     * @return
     */
    List<UserEmail> findListByEmail(String email);


    List<UserEmail> findListByUserMainId(Long userMainId);


    PageInfo<UserEmail> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo);
}
