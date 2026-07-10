package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;
import lombok.Data;

/**
 * 管理员信息VO
 */
@Data
public class AdminInfoVO extends AdminInfo {

    /**
     * 账号名称
     */
    private String adminName;
}
