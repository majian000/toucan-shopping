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
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<UserEmail> findListByEntityNothingDeleteStatus(UserEmail entity);

    int updateDeleteStatus(Short deleteStatus,Long userMainId,String email);

    /**
     * 根据邮箱查询关联
     * @param email
     * @return
     */
    List<UserEmail> findListByEmail(String email);

    /**
     * 根据邮箱查询关联
     * @param email
     * @return
     */
    List<UserEmail> findListByEmail(String email,List<Long> userMianIdList);

    List<UserEmail> findListByUserMainId(Long userMainId);


    PageInfo<UserEmail> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo);


    /**
     * 删除指定用户ID下所有关联邮箱
     * @param userMainId
     * @return
     */
    int deleteByUserMainId(Long userMainId);


    /**
     * 查询有效的邮箱关联
     * @param userMainId
     * @return
     */
    UserEmail findByUserMainId(Long userMainId);
}
