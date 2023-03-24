package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserEmail;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import lombok.Data;

/**
 * 用户与手机号子表
 */
@Data
public class UserMobilePhoneVO extends UserMobilePhone {


    /**
     * 所属应用
     */
    private String appCode ="10001001";

}
