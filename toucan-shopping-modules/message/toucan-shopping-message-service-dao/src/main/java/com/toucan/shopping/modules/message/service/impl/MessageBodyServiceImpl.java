package com.toucan.shopping.modules.message.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.mapper.MessageBodyMapper;
import com.toucan.shopping.modules.message.page.MessageBodyPageInfo;
import com.toucan.shopping.modules.message.service.MessageBodyService;
import com.toucan.shopping.modules.message.vo.MessageBodyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息内容服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
@Service
public class MessageBodyServiceImpl implements MessageBodyService {

    @Autowired
    private MessageBodyMapper messageBodyMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(MessageBody entity) {
        entity.setShardingDate(entity.getCreateDate());
        return messageBodyMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return messageBodyMapper.deleteById(id);
    }

    @Override
    public int update(MessageBody entity) {
        return messageBodyMapper.update(entity);
    }

    @Override
    public List<MessageBody> findListByEntity(MessageBody query) {
        return messageBodyMapper.findListByEntity(query);
    }

    @Override
    public List<MessageBody> queryList(MessageBody query) {
        return messageBodyMapper.queryList(query);
    }

    @Override
    public PageInfo<MessageBodyVO> queryListPage(MessageBodyPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<MessageBodyVO> pageInfo = new PageInfo();
        pageInfo.setList(messageBodyMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(messageBodyMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

}
