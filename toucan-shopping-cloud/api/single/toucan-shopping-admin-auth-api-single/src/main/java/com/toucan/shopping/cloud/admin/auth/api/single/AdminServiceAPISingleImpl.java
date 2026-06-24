package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.admin.AdminController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceAPISingleImpl implements AdminServiceAPI {

    @Autowired
    private AdminController adminController;

    @Override
    public ResultObjectVO login(String signHeader, RequestJsonVO requestVo) {
        return adminController.login(requestVo);
    }

    @Override
    public ResultObjectVO queryLoginToken(String signHeader, RequestJsonVO requestVo) {
        return adminController.queryLoginToken(requestVo);
    }

    @Override
    public ResultObjectVO isOnline(String signHeader, RequestJsonVO requestVo) {
        return adminController.isOnline(requestVo);
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return adminController.queryListByEntity(requestVo);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return adminController.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return adminController.update(requestVo);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return adminController.list(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return adminController.findById(requestVo);
    }

    @Override
    public ResultObjectVO logout(String signHeader, RequestJsonVO requestVo) {
        return adminController.logout(requestVo);
    }

    @Override
    public ResultObjectVO updatePassword(String signHeader, RequestJsonVO requestVo) {
        return adminController.updatePassword(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return adminController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return adminController.deleteByIds(requestVo);
    }
}
