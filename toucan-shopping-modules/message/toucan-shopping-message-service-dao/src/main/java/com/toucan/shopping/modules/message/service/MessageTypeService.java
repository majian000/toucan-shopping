package com.toucan.shopping.modules.message.service;


import com.toucan.shopping.modules.message.entity.MessageType;

import java.util.List;

/**
 * 收货地址服务
 * @author majian
 * @date 2021-8-4 17:44:57
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

}
