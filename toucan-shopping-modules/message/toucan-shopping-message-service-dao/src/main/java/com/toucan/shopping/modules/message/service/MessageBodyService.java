package com.toucan.shopping.modules.message.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.page.MessageBodyPageInfo;
import com.toucan.shopping.modules.message.vo.MessageBodyVO;

import java.util.List;

/**
 * 消息内容服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
public interface MessageBodyService {

    int save(MessageBody entity);

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
    int update(MessageBody entity);


    List<MessageBody> findListByEntity(MessageBody query);


    List<MessageBody> queryList(MessageBody query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<MessageBodyVO> queryListPage(MessageBodyPageInfo pageInfo);
}
