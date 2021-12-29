package com.toucan.shopping.modules.message.mapper;

import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.page.MessageBodyPageInfo;
import com.toucan.shopping.modules.message.vo.MessageBodyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MessageBodyMapper {

    int insert(MessageBody entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(MessageBody entity);

    List<MessageBody> findListByEntity(MessageBody query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<MessageBodyVO> queryListPage(MessageBodyPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(MessageBodyPageInfo pageInfo);
}
