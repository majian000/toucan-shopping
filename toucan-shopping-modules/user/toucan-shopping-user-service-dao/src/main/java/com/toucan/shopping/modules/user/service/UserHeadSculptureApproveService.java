package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;

import java.util.List;

/**
 * 用户头像审核
 */
public interface UserHeadSculptureApproveService {

    /**
     * 保存实名审核对象
     * @param entity
     * @return
     */
    int save(UserHeadSculptureApprove entity);

    /**
     * 修改实名审核对象
     * @param entity
     * @return
     */
    int update(UserHeadSculptureApprove entity);


    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<UserHeadSculptureApprove> findListByEntity(UserHeadSculptureApprove entity);


    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<UserHeadSculptureApprove> findListByEntityOrderByCreateDateDesc(UserHeadSculptureApprove entity);


    /**
     * 查询列表根据创建时间倒序
     * @param entity
     * @return
     */
    List<UserHeadSculptureApprove> findListByEntityOrderByUpdateDateDesc(UserHeadSculptureApprove entity);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<UserHeadSculptureApprove> queryListPage(UserHeadSculptureApprovePageInfo queryPageInfo);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
