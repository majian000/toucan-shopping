package com.toucan.shopping.modules.message.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.entity.MessageUser;
import com.toucan.shopping.modules.message.mapper.MessageUserMapper;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.service.MessageUserService;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户消息服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
@Service
public class MessageUserServiceImpl implements MessageUserService {

    @Autowired
    private MessageUserMapper messageBodyMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(MessageUser entity) {
        return messageBodyMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return messageBodyMapper.deleteById(id);
    }


    @Override
    public List<MessageUser> findListByEntity(MessageUser query) {
        return messageBodyMapper.findListByEntity(query);
    }

    @Override
    public PageInfo<MessageUserVO> queryListPage(MessageUserPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<MessageUserVO> pageInfo = new PageInfo();
        pageInfo.setList(messageBodyMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(messageBodyMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

}
