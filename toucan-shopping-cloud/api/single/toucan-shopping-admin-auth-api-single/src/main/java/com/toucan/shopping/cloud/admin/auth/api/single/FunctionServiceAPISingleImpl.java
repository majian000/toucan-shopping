package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.function.FunctionController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionServiceAPISingleImpl implements FunctionServiceAPI {

    @Autowired
    private FunctionController functionController;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO saves(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTableByPid(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByAppCode(RequestJsonVO requestVo) {
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
    public ResultObjectVO queryAppFunctionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeByPid(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryFunctionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryAdminAppFunctions(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryChildren(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(String signHeader, RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return null;
    }
}
