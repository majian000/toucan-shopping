package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminOrgnazitionServiceAPI {

     ResultObjectVO save(RequestJsonVO requestVo);


     ResultObjectVO queryListByEntity(RequestJsonVO requestVo);


     ResultObjectVO deleteByAppCode(RequestJsonVO requestVo);


     ResultObjectVO queryAppListByAdminId(RequestJsonVO requestVo);


     /**
    * 保存组织机构关联
    * @param requestJsonVO
    * @return
    */
     ResultObjectVO saveOrgnazitions(RequestJsonVO requestJsonVO);

}
