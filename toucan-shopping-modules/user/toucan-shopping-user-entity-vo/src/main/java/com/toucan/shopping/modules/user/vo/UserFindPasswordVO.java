package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.Data;

import java.util.List;

@Data
public class UserFindPasswordVO extends UserVO {

    /**
     * 手机号、邮箱、用户名
     */
    private String findUserName;


    /**
     * 要登录的应用
     */
    private String appCode;


}
