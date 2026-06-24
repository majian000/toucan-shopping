package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminAppServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminAppBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminAppServiceAPISingleImpl implements AdminAppServiceAPI {

    @Autowired
    private AdminAppBusinessService adminAppBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return adminAppBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return adminAppBusinessService.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO deleteByAppCode(String signHeader, RequestJsonVO requestVo) {
        return adminAppBusinessService.deleteByAppCode(requestVo);
    }

    @Override
    public ResultObjectVO queryAppListByAdminId(String signHeader, RequestJsonVO requestVo) {
        return adminAppBusinessService.queryAppListByAdminId(requestVo);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return adminAppBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO onlineList(RequestJsonVO requestVo) {
        return adminAppBusinessService.onlineList(requestVo);
    }

    @Override
    public ResultObjectVO loginList(RequestJsonVO requestVo) {
        return adminAppBusinessService.loginList(requestVo);
    }

    @Override
    public ResultObjectVO batchUpdateLoginStatus(RequestJsonVO requestVo) {
        return adminAppBusinessService.batchUpdateLoginStatus(requestVo);
    }

    @Override
    public ResultObjectVO logout(RequestJsonVO requestVo) {
        return adminAppBusinessService.logout(requestVo);
    }

    @Override
    public ResultObjectVO queryAppLoginUserCountList(RequestJsonVO requestVo) {
        return adminAppBusinessService.queryAppLoginUserCountList(requestVo);
    }
}
