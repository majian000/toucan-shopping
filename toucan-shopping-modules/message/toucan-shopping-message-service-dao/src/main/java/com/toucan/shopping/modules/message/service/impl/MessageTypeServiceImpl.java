package com.toucan.shopping.modules.message.service.impl;

import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.mapper.MessageTypeMapper;
import com.toucan.shopping.modules.message.service.MessageTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class MessageTypeServiceImpl implements MessageTypeService {

    @Autowired
    private MessageTypeMapper messageTypeMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(MessageType entity) {
        return messageTypeMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return messageTypeMapper.deleteById(id);
    }

    @Override
    public int update(MessageType entity) {
        return messageTypeMapper.update(entity);
    }

    @Override
    public List<MessageType> findListByEntity(MessageType query) {
        return messageTypeMapper.findListByEntity(query);
    }

}
