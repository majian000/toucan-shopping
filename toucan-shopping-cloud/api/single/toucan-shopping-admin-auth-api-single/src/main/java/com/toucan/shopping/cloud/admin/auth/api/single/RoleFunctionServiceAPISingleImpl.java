package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class RoleFunctionServiceAPISingleImpl implements RoleFunctionServiceAPI {

    @Override
    public ResultObjectVO saveFunctions(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryRoleFunctionList(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO refreshCache(RequestJsonVO requestJsonVO) {
        return null;
    }
}
