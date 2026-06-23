package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class AppServiceAPISingleImpl implements AppServiceAPI {

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO listPage(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findByCode(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO enableStatusByCode(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryListByCodes(RequestJsonVO requestVo) {
        return null;
    }
}
