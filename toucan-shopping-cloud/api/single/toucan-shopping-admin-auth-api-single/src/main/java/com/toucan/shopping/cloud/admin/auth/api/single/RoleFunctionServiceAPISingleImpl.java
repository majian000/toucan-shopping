package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.role.RoleFunctionController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleFunctionServiceAPISingleImpl implements RoleFunctionServiceAPI {

    @Autowired
    private RoleFunctionController roleFunctionController;

    @Override
    public ResultObjectVO saveFunctions(String signHeader, RequestJsonVO requestJsonVO) {
        return roleFunctionController.saveFunctions(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryRoleFunctionList(String signHeader, RequestJsonVO requestJsonVO) {
        return roleFunctionController.queryRoleFunctionList(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return roleFunctionController.list(requestVo);
    }

    @Override
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(RequestJsonVO requestVo) {
        return roleFunctionController.queryFunctionTreeByRoleIdAndParentId(requestVo);
    }

    @Override
    public ResultObjectVO refreshCache(RequestJsonVO requestJsonVO) {
        return roleFunctionController.refreshCache(requestJsonVO);
    }
}
