package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserEmail;
import com.toucan.shopping.modules.user.entity.UserUserName;
import lombok.Data;

/**
 * 用户与用户名子表
 */
@Data
public class UserUserNameVO extends UserUserName {


    /**
     * 所属应用
     */
    private String appCode ="10001001";

}
