package com.toucan.shopping.modules.admin.auth.log.vo;

import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 操作日志列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OperateLogPageInfo extends PageInfo<OperateLogVO> {


    // ===============查询条件===================

    /**
     * 所属管理员账号ID
     */
    private String adminId;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 应用编码
     */
    private String appCode;


    //==============================================
}
