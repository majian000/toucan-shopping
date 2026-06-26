package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminOrgnazitionServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminOrgnazitionBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOrgnazitionServiceAPISingleImpl implements AdminOrgnazitionServiceAPI {

    @Autowired
    private AdminOrgnazitionBusinessService adminOrgnazitionBusinessService;

    @Override
    public ResultObjectVO save( RequestJsonVO requestVo) {
        return adminOrgnazitionBusinessService.saveOrgnazitions(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity( RequestJsonVO requestVo) {
        return adminOrgnazitionBusinessService.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO deleteByAppCode( RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppListByAdminId( RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO saveOrgnazitions( RequestJsonVO requestJsonVO) {
        return adminOrgnazitionBusinessService.saveOrgnazitions(requestJsonVO);
    }
}
