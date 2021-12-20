package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.mapper.UserHeadSculptureApproveMapper;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;
import com.toucan.shopping.modules.user.service.UserHeadSculptureApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHeadSculptureApproveServiceImpl implements UserHeadSculptureApproveService {

    @Autowired
    private UserHeadSculptureApproveMapper userHeadSculptureApproveMapper;


    @Override
    public int save(UserHeadSculptureApprove entity) {
        return userHeadSculptureApproveMapper.insert(entity);
    }

    @Override
    public int update(UserHeadSculptureApprove entity) {
        return userHeadSculptureApproveMapper.update(entity);
    }


    @Override
    public List<UserHeadSculptureApprove> findListByEntity(UserHeadSculptureApprove entity) {
        return userHeadSculptureApproveMapper.findListByEntity(entity);
    }


    @Override
    public List<UserHeadSculptureApprove> findListByEntityOrderByCreateDateDesc(UserHeadSculptureApprove entity) {
        return userHeadSculptureApproveMapper.findListByEntityOrderByCreateDateDesc(entity);
    }


    @Override
    public List<UserHeadSculptureApprove> findListByEntityOrderByUpdateDateDesc(UserHeadSculptureApprove entity) {
        return userHeadSculptureApproveMapper.findListByEntityOrderByUpdateDateDesc(entity);
    }


    @Override
    public PageInfo<UserHeadSculptureApprove> queryListPage(UserHeadSculptureApprovePageInfo queryPageInfo) {
        PageInfo<UserHeadSculptureApprove> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userHeadSculptureApproveMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(userHeadSculptureApproveMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return userHeadSculptureApproveMapper.deleteById(id);
    }
}
