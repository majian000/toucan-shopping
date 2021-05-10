package com.toucan.shopping.modules.user.service;




import com.toucan.shopping.modules.user.entity.UserUserName;

import java.util.List;

public interface UserUserNameService {

    List<UserUserName> findListByEntity(UserUserName entity);

    int save(UserUserName entity);


    List<UserUserName> queryListByUserId(Long[] userIdArray);


    /**
     * 根据用户名查询关联
     * @param username
     * @return
     */
    List<UserUserName> findListByUserName(String username);
}
