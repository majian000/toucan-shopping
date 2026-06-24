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
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO onlineList(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO loginList(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO batchUpdateLoginStatus(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO logout(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppLoginUserCountList(RequestJsonVO requestVo) {
        return null;
    }
}
