package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 登录历史列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AdminLoginHistoryPageInfo extends PageInfo<AdminLoginHistoryVO> {

    /**
     * 所属账号ID
     */
    private String adminId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录源头 1:PC
     */
    private Integer loginSrcType;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
