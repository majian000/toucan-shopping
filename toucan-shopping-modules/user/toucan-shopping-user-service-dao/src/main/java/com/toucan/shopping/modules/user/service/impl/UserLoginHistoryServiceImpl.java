package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserLoginHistory;
import com.toucan.shopping.modules.user.mapper.UserLoginHistoryMapper;
import com.toucan.shopping.modules.user.page.UserLoginHistoryPageInfo;
import com.toucan.shopping.modules.user.service.UserLoginHistoryService;
import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户登录历史服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService {


    @Autowired
    private UserLoginHistoryMapper userLoginHistoryMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(UserLoginHistory entity) {
        return userLoginHistoryMapper.insert(entity);
    }

    @Override
    public PageInfo<UserLoginHistory> queryListPage(UserLoginHistoryPageInfo queryPageInfo) {
        PageInfo<UserLoginHistory> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(userLoginHistoryMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(userLoginHistoryMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


    @Override
    public List<UserLoginHistory> queryListByCreateDateDesc(UserLoginHistoryVO query) {
        return userLoginHistoryMapper.queryListByCreateDateDesc(query);
    }

}
