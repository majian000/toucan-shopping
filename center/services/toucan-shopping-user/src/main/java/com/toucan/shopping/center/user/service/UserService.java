package com.toucan.shopping.center.user.service;


import com.github.pagehelper.PageInfo;
import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.page.UserPageInfo;
import com.toucan.shopping.center.user.vo.UserVO;

import java.util.List;

public interface UserService {

    List<User> findListByEntity(User user);

    int save(User user);

    int deleteById(Long id);

    List<User> findById(Long id);


    /**
     * 添加这个手机号到应用
     * @param mobile
     * @param appCode
     * @return
     */
    public int addUserToAppCode(String mobile, String appCode);


    /**
     * 查询列表页
     * @param appPageInfo
     * @return
     */
    PageInfo<UserVO> queryListPage(UserPageInfo appPageInfo);
}
