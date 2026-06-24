package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.modules.admin.auth.controller.dict.DictController;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceAPISingleImpl implements DictServiceAPI {

    @Autowired
    private DictController dictController;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryDictByCodeAndCategoryCode(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode(RequestJsonVO requestJsonVO) {
        return null;
    }
}
