package com.toucan.shopping.modules.message.mapper;

import com.toucan.shopping.modules.message.entity.MessageUser;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MessageUserMapper {

    int insert(MessageUser entity);

    int inserts(List<MessageUserVO> entitys);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    List<MessageUser> findListByEntity(MessageUser query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<MessageUserVO> queryListPage(MessageUserPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(MessageUserPageInfo pageInfo);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<MessageUserVO> queryMyListPage(MessageUserPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryMyListPageCount(MessageUserPageInfo pageInfo);

    Long queryListCount(MessageUserVO query);
}
