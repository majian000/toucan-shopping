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
    private MessageUserMapper messageUserMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(MessageUser entity) {
        return messageUserMapper.insert(entity);
    }

    @Override
    public int saves(List<MessageUserVO> entitys) {
        return messageUserMapper.inserts(entitys);
    }


    @Override
    public int deleteById(Long id) {
        return messageUserMapper.deleteById(id);
    }

    @Override
    public int update(MessageUser entity) {
        return messageUserMapper.update(entity);
    }


    @Override
    public List<MessageUser> findListByEntity(MessageUser query) {
        return messageUserMapper.findListByEntity(query);
    }

    @Override
    public Long queryListCount(MessageUserVO query) {
        return messageUserMapper.queryListCount(query);
    }

    @Override
    public PageInfo<MessageUserVO> queryListPage(MessageUserPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<MessageUserVO> pageInfo = new PageInfo();
        pageInfo.setList(messageUserMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(messageUserMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public PageInfo<MessageUserVO> queryMyListPage(MessageUserPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<MessageUserVO> pageInfo = new PageInfo();
        pageInfo.setList(messageUserMapper.queryMyListPage(queryPageInfo));
        pageInfo.setTotal(messageUserMapper.queryMyListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int updateStatus(MessageUserVO messageUserVO) {
        return messageUserMapper.updateStatus(messageUserVO);
    }


    @Override
    public int updateAllReadStatus(Long userMainId) {
        return messageUserMapper.updateAllReadStatus(userMainId);
    }

}
