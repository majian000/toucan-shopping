package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.OrgnazitionServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.OrgnazitionBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgnazitionServiceAPISingleImpl implements OrgnazitionServiceAPI {

    @Autowired
    private OrgnazitionBusinessService orgnazitionBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryAppOrgnazitionTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionBusinessService.queryAppOrgnazitionTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return orgnazitionBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryOrgnazationTree(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionBusinessService.queryOrgnazationTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAdminOrgnazitionTree(String signHeader, RequestJsonVO requestJsonVO) {
        return orgnazitionBusinessService.queryAdminOrgnazitionTree(requestJsonVO);
    }
}
