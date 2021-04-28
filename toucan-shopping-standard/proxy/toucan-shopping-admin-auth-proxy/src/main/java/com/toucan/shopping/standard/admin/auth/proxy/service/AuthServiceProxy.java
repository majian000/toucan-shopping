package com.toucan.shopping.standard.admin.auth.proxy.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AuthServiceProxy {



    public ResultObjectVO verify(  RequestJsonVO requestVo);

}
