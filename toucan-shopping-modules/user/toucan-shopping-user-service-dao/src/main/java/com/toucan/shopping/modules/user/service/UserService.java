package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserVO;

import java.util.List;

public interface UserService {

    List<User> findListByEntity(User user);

    List<User> findListByUserMainId(Long userMainId);

    int save(User user);

    int deleteById(Long id);

    List<User> findById(Long id);

    int updateEnableStatus(Short enableStatus,Long userMainId);

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

    /**
     * 根据用户ID修改密码
     * @param userMainId
     * @param password
     * @return
     */
    int updatePasswordByUserMainId(Long userMainId,String password);
}
