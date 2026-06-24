package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.app.AppController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServiceAPISingleImpl implements AppServiceAPI {

    @Autowired
    private AppController appController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return appController.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return appController.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(String signHeader, RequestJsonVO requestVo) {
        return appController.listPage(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return appController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return appController.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return appController.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return appController.list(requestVo);
    }

    @Override
    public ResultObjectVO findByCode(String signHeader, RequestJsonVO requestVo) {
        return appController.findByCode(requestVo);
    }

    @Override
    public ResultObjectVO enableStatusByCode(RequestJsonVO requestVo) {
        return appController.queryEnableStatusByCode(requestVo);
    }

    @Override
    public ResultObjectVO queryListByCodes(RequestJsonVO requestVo) {
        return appController.queryListByCodes(requestVo);
    }
}
