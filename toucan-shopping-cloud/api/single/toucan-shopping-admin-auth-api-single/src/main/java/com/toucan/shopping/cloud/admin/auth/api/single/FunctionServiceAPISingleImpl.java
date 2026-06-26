package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.FunctionBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionServiceAPISingleImpl implements FunctionServiceAPI {

    @Autowired
    private FunctionBusinessService functionBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return functionBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO saves(RequestJsonVO requestVo) {
        return functionBusinessService.saves(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return functionBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTable(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryAppFunctionTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeTableByPid(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryAppFunctionTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return functionBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByAppCode(RequestJsonVO requestVo) {
        return functionBusinessService.deleteByAppCode(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return functionBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return functionBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryAppFunctionTree(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryAppFunctionTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAppFunctionTreeByPid(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryAppFunctionTreeByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryFunctionTree(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryFunctionTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAdminAppFunctions(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryAdminAppFunctions(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryChildren(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryChildren(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(RequestJsonVO requestJsonVO) {
        return functionBusinessService.queryOneChildsByAdminIdAndAppCodeAndParentUrl(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return functionBusinessService.list(requestVo);
    }
}
