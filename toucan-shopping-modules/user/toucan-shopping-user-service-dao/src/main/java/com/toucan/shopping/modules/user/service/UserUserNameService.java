package com.toucan.shopping.modules.user.service;




import com.toucan.shopping.modules.user.entity.UserUserName;

import java.util.List;

public interface UserUserNameService {

    List<UserUserName> findListByEntity(UserUserName entity);

    int save(UserUserName entity);


    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<UserUserName> findListByEntityNothingDeleteStatus(UserUserName entity);

    List<UserUserName> queryListByUserId(Long[] userIdArray);


    List<UserUserName> findListByUserMainId(Long userMainId);

    /**
     * 根据用户名查询关联
     * @param username
     * @return
     */
    List<UserUserName> findListByUserName(String username);
}
