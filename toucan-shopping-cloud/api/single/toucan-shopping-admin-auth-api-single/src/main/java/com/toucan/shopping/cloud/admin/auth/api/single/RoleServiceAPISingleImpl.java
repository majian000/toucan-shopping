package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.role.RoleController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceAPISingleImpl implements RoleServiceAPI {

    @Autowired
    private RoleController roleController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return roleController.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return roleController.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(String signHeader, RequestJsonVO requestVo) {
        return roleController.listPage(requestVo);
    }

    @Override
    public ResultObjectVO queryAdminRoleTree(String signHeader, RequestJsonVO requestJsonVO) {
        return roleController.queryAdminRoleTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return roleController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return roleController.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return roleController.deleteByIds(requestVo);
    }
}
