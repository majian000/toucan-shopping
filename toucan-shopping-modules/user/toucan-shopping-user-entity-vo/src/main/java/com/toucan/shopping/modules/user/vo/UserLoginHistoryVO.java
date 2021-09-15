package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserLoginHistory;
import lombok.Data;

/**
 * 用户登录信息
 */
@Data
public class UserLoginHistoryVO extends UserLoginHistory {

    /**
     * 查询的数量
     */
    private Integer size;

}
