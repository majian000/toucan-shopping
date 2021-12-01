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

}
