package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminRoleServiceAPI {

    public ResultObjectVO saveRoles(RequestJsonVO requestJsonVO);



    ResultObjectVO queryListByEntity(   RequestJsonVO requestVo);


    /**
     * 列表分页

     * @param requestVo
     * @return
     */
    ResultObjectVO list(  RequestJsonVO requestVo);


}
