package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminOrgnazitionServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class AdminOrgnazitionServiceAPISingleImpl implements AdminOrgnazitionServiceAPI {


    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByAppCode(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppListByAdminId(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO saveOrgnazitions(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }
}
