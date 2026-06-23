package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminRoleServiceAPI {

    public ResultObjectVO saveRoles( String signHeader, RequestJsonVO requestJsonVO);



    ResultObjectVO queryListByEntity( String signHeader,  RequestJsonVO requestVo);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO list( String signHeader, RequestJsonVO requestVo);


}
