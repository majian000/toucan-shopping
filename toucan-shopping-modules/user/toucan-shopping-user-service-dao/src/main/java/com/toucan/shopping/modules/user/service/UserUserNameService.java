package com.toucan.shopping.modules.user.service;




import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserUserName;
import com.toucan.shopping.modules.user.page.UserPageInfo;

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


    PageInfo<UserUserName> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo);


    int updateDeleteStatus(Short deleteStatus,Long userMainId,String username);


    /**
     * 删除指定用户ID下所有关联用户名
     * @param userMainId
     * @return
     */
    int deleteByUserMainId(Long userMainId);
}
