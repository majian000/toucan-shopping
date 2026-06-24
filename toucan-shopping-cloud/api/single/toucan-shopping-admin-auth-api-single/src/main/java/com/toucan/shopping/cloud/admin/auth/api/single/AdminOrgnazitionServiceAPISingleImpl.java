package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminOrgnazitionServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.admin.AdminOrgnazitionController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOrgnazitionServiceAPISingleImpl implements AdminOrgnazitionServiceAPI {

    @Autowired
    private AdminOrgnazitionController adminOrgnazitionController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return adminOrgnazitionController.saveOrgnazitions(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return adminOrgnazitionController.queryListByEntity(requestVo);
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
        return adminOrgnazitionController.saveOrgnazitions(requestJsonVO);
    }
}
