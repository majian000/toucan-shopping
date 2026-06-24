package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminAppServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.admin.AdminAppController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminAppServiceAPISingleImpl implements AdminAppServiceAPI {

    @Autowired
    private AdminAppController adminAppController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return adminAppController.save(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return adminAppController.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO deleteByAppCode(String signHeader, RequestJsonVO requestVo) {
        return adminAppController.deleteByAppCode(requestVo);
    }

    @Override
    public ResultObjectVO queryAppListByAdminId(String signHeader, RequestJsonVO requestVo) {
        return adminAppController.queryAppListByAdminId(requestVo);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return adminAppController.list(requestVo);
    }

    @Override
    public ResultObjectVO onlineList(RequestJsonVO requestVo) {
        return adminAppController.onlineList(requestVo);
    }

    @Override
    public ResultObjectVO loginList(RequestJsonVO requestVo) {
        return adminAppController.loginList(requestVo);
    }

    @Override
    public ResultObjectVO batchUpdateLoginStatus(RequestJsonVO requestVo) {
        return adminAppController.batchUpdateLoginStatus(requestVo);
    }

    @Override
    public ResultObjectVO logout(RequestJsonVO requestVo) {
        return adminAppController.logout(requestVo);
    }

    @Override
    public ResultObjectVO queryAppLoginUserCountList(RequestJsonVO requestVo) {
        return adminAppController.queryAppLoginUserCountList(requestVo);
    }
}
