package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.Data;

import java.util.List;

/**
 * 重置密码
 */
@Data
public class UserResetPasswordVO extends UserVO {

    /**
     * 用户所属应用
     */
    private List<UserApp> userApps;

}
