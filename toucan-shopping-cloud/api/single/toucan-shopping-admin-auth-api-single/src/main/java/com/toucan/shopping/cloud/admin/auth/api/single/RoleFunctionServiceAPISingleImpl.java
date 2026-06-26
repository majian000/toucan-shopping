package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.RoleFunctionBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleFunctionServiceAPISingleImpl implements RoleFunctionServiceAPI {

    @Autowired
    private RoleFunctionBusinessService roleFunctionBusinessService;

    @Override
    public ResultObjectVO saveFunctions(RequestJsonVO requestJsonVO) {
        return roleFunctionBusinessService.saveFunctions(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryRoleFunctionList(RequestJsonVO requestJsonVO) {
        return roleFunctionBusinessService.queryRoleFunctionList(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return roleFunctionBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(RequestJsonVO requestVo) {
        return roleFunctionBusinessService.queryFunctionTreeByRoleIdAndParentId(requestVo);
    }

    @Override
    public ResultObjectVO refreshCache(RequestJsonVO requestJsonVO) {
        return roleFunctionBusinessService.refreshCache(requestJsonVO);
    }
}
