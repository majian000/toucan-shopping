package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceAPISingleImpl implements AdminServiceAPI {


    @Override
    public ResultObjectVO login(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryLoginToken(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO isOnline(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryListByEntity(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO logout(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO updatePassword(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return null;
    }
}
