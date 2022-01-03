package com.toucan.shopping.modules.message.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.entity.MessageUser;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.vo.MessageUserVO;

import java.util.List;

/**
 * 用户消息服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
public interface MessageUserService {

    int save(MessageUser entity);

    int saves(List<MessageUserVO> entitys);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);



    List<MessageUser> findListByEntity(MessageUser query);

    Long queryListCount(MessageUserVO query);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<MessageUserVO> queryListPage(MessageUserPageInfo pageInfo);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<MessageUserVO> queryMyListPage(MessageUserPageInfo pageInfo);

}
