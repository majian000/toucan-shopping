package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.modules.admin.auth.business.service.DictBusinessService;
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
    private DictBusinessService dictBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return dictBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return dictBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return dictBusinessService.listPage(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return dictBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return dictBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return dictBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return dictBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        return dictBusinessService.queryTreeChildByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryDictByCodeAndCategoryCode(RequestJsonVO requestJsonVO) {
        return dictBusinessService.queryDictByCodeAndCategoryCode(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode(RequestJsonVO requestJsonVO) {
        return dictBusinessService.queryDictByCodesAndCategoryCode(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeAll(RequestJsonVO requestJsonVO) {
        return dictBusinessService.queryTreeAll(requestJsonVO);
    }
}
