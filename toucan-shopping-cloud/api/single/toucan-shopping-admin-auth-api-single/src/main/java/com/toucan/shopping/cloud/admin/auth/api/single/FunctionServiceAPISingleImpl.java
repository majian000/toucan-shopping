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
        return functionController.save(requestVo);
    }

    @Override
    public ResultObjectVO saves(RequestJsonVO requestVo) {
        return functionController.saves(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return functionController.update(requestVo);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryAppFunctionTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTableByPid(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryAppFunctionTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return functionController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByAppCode(RequestJsonVO requestVo) {
        return functionController.deleteByAppCode(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return functionController.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return functionController.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryAppFunctionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryAppFunctionTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeByPid(RequestJsonVO requestJsonVO) {
        return functionController.queryAppFunctionTreeByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryFunctionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryFunctionTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAdminAppFunctions(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryAdminAppFunctions(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryChildren(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryChildren(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(String signHeader, RequestJsonVO requestJsonVO) {
        return functionController.queryOneChildsByAdminIdAndAppCodeAndParentUrl(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return functionController.list(requestVo);
    }
}
