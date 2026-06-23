package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminOrgnazitionServiceAPI {

    ResultObjectVO save( String signHeader,  RequestJsonVO requestVo);


    ResultObjectVO queryListByEntity( String signHeader,  RequestJsonVO requestVo);


    ResultObjectVO deleteByAppCode( String signHeader,  RequestJsonVO requestVo);


    ResultObjectVO queryAppListByAdminId( String signHeader,  RequestJsonVO requestVo);


    /**
     * 保存组织机构关联
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO saveOrgnazitions( String signHeader, RequestJsonVO requestJsonVO);

}
