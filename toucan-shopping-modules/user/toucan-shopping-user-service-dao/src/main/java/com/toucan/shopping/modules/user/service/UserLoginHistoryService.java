package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserLoginHistory;
import com.toucan.shopping.modules.user.page.UserLoginHistoryPageInfo;
import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;

import java.util.List;

/**
 * 用户登录历史服务
 * @author majian
 * @date 2021-8-24 16:11:11
 */
public interface UserLoginHistoryService {


    int save(UserLoginHistory entity);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<UserLoginHistory> queryListPage(UserLoginHistoryPageInfo queryPageInfo);

    /**
     * 查询某个人最新登录的几条数据
     * @param query
     * @return
     */
    List<UserLoginHistory> queryListByCreateDateDesc(UserLoginHistoryVO query);

}
