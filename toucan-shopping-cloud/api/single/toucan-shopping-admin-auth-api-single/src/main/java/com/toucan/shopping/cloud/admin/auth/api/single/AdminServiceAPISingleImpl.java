package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AdminBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceAPISingleImpl implements AdminServiceAPI {

    @Autowired
    private AdminBusinessService adminBusinessService;

    @Override
    public ResultObjectVO login(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.login(requestVo);
    }

    @Override
    public ResultObjectVO queryLoginToken(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.queryLoginToken(requestVo);
    }

    @Override
    public ResultObjectVO isOnline(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.isOnline(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO logout(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.logout(requestVo);
    }

    @Override
    public ResultObjectVO updatePassword(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.updatePassword(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return adminBusinessService.deleteByIds(requestVo);
    }
}
