package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.OrgnazitionServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.orgnization.OrgnazitionController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgnazitionServiceAPISingleImpl implements OrgnazitionServiceAPI {

    @Autowired
    private OrgnazitionController orgnazitionController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionController.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionController.update(requestVo);
    }

    @Override
    public ResultObjectVO queryAppOrgnazitionTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionController.queryAppOrgnazitionTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionController.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionController.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryOrgnazationTree(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionController.queryOrgnazationTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAdminOrgnazitionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionController.queryAdminOrgnazitionTree(requestJsonVO);
    }
}
