package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;

import java.util.List;

/**
 * 用户实名审核
 */
public interface UserTrueNameApproveService {

    /**
     * 保存实名审核对象
     * @param entity
     * @return
     */
    int save(UserTrueNameApprove entity);

    /**
     * 保存实名审核对象
     * @param entity
     * @return
     */
    int update(UserTrueNameApprove entity);


    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity);



    /**
     * 查询列表根据创建时间倒序
     * @param entity
     * @return
     */
    List<UserTrueNameApprove> findListByEntityOrderByUpdateDateDesc(UserTrueNameApprove entity);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<UserTrueNameApprove> queryListPage(UserTrueNameApprovePageInfo queryPageInfo);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
