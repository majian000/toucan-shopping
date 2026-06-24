package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.AppBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServiceAPISingleImpl implements AppServiceAPI {

    @Autowired
    private AppBusinessService appBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.listPage(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO findByCode(String signHeader, RequestJsonVO requestVo) {
        return appBusinessService.findByCode(requestVo);
    }

    @Override
    public ResultObjectVO enableStatusByCode(RequestJsonVO requestVo) {
        return appBusinessService.queryEnableStatusByCode(requestVo);
    }

    @Override
    public ResultObjectVO queryListByCodes(RequestJsonVO requestVo) {
        return appBusinessService.queryListByCodes(requestVo);
    }
}
