package com.toucan.shopping.modules.message.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.page.MessageTypePageInfo;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;

import java.util.List;

/**
 * 消息类型服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
public interface MessageTypeService {

    int save(MessageType entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 修改
     * @param entity
     * @return
     */
    int update(MessageType entity);


    List<MessageType> findListByEntity(MessageType query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<MessageTypeVO> queryListPage(MessageTypePageInfo pageInfo);



    List<MessageTypeVO> queryList(MessageTypeVO query);
}
