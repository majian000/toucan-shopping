package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.DictCategoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.dict.DictCategoryController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictCategoryServiceAPISingleImpl implements DictCategoryServiceAPI {

    @Autowired
    private DictCategoryController dictCategoryController;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return dictCategoryController.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return dictCategoryController.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return dictCategoryController.listPage(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return dictCategoryController.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return dictCategoryController.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return dictCategoryController.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestVo) {
        return dictCategoryController.queryList(requestVo);
    }
}
