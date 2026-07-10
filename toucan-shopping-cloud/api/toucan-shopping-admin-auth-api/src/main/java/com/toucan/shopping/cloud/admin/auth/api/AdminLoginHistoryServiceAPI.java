package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 登录历史服务API
 */
public interface AdminLoginHistoryServiceAPI {

    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage(RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);
}
