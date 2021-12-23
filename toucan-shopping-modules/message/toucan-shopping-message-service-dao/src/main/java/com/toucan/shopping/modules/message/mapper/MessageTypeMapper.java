package com.toucan.shopping.modules.message.mapper;

import com.toucan.shopping.modules.message.entity.MessageType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MessageTypeMapper {

    int insert(MessageType entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(MessageType entity);

    List<MessageType> findListByEntity(MessageType query);

}
