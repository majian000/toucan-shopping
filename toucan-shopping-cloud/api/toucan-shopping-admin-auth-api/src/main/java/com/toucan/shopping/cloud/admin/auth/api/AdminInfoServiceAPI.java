package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 管理员信息服务API
 */
public interface AdminInfoServiceAPI {

    /**
     * 保存/更新管理员信息
     * @param requestVo
     * @return
     */
    ResultObjectVO saveOrUpdate(RequestJsonVO requestVo);

    /**
     * 根据adminId查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByAdminId(RequestJsonVO requestVo);
}
