package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.RoleBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceAPISingleImpl implements RoleServiceAPI {

    @Autowired
    private RoleBusinessService roleBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return roleBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return roleBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return roleBusinessService.listPage(requestVo);
    }

    @Override
    public ResultObjectVO queryAdminRoleTree(RequestJsonVO requestJsonVO) {
        return roleBusinessService.queryAdminRoleTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return roleBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return roleBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return roleBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryDetail(RequestJsonVO requestVo) {
        return roleBusinessService.queryDetail(requestVo);
    }
}
