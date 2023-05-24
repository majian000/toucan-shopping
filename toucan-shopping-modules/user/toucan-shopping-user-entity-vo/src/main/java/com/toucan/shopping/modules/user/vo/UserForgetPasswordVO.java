package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.Data;

import java.util.List;

/**
 * 找回密码
 */
@Data
public class UserForgetPasswordVO extends UserVO {

    /**
     * 用户所属应用
     */
    private List<UserApp> userApps;

    /**
     * 验证方式 0:手机号 1:邮箱验证 2:身份证验证
     */
    private int verifyMethod;

}
