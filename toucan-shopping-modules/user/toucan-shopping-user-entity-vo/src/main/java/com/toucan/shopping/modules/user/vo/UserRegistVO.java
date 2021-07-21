package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserRegistVO extends UserVO {

    /**
     * 用户所属应用
     */
    private List<UserApp> userApps;

    /**
     * 接受用户协议
     */
    private Integer acceptUserDoc;

}
