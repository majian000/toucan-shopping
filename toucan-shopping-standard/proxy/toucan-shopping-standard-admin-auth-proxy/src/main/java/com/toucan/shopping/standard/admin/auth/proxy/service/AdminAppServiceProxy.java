package com.toucan.shopping.standard.admin.auth.proxy.service;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminAppServiceProxy {

    ResultObjectVO save( RequestJsonVO requestVo);


    ResultObjectVO queryListByEntity(RequestJsonVO requestVo);


    ResultObjectVO deleteByAppCode(  RequestJsonVO requestVo);


    ResultObjectVO queryAppListByAdminId(  RequestJsonVO requestVo);



}
