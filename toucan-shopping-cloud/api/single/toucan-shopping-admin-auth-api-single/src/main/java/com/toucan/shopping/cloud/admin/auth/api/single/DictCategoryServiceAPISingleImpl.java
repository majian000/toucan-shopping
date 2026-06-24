package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.DictCategoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.DictCategoryBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictCategoryServiceAPISingleImpl implements DictCategoryServiceAPI {

    @Autowired
    private DictCategoryBusinessService dictCategoryBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.listPage(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestVo) {
        return dictCategoryBusinessService.queryList(requestVo);
    }
}
