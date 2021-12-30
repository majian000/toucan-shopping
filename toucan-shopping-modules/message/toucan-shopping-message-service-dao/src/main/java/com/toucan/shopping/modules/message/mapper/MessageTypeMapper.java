package com.toucan.shopping.modules.message.mapper;

import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.page.MessageTypePageInfo;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MessageTypeMapper {

    int insert(MessageType entity);


    List<MessageTypeVO> queryList(MessageTypeVO entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(MessageType entity);

    List<MessageType> findListByEntity(MessageType query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<MessageTypeVO> queryListPage(MessageTypePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(MessageTypePageInfo pageInfo);
}
