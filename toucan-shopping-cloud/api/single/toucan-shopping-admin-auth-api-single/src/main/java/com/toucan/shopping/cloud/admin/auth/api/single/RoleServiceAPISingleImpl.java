package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceAPISingleImpl implements RoleServiceAPI {


    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO listPage(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryAdminRoleTree(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return null;
    }
}
