package com.toucan.shopping.cloud.common.data.api.single;

import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.modules.area.business.service.AreaBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceSingleImpl implements AreaServiceAPI {

    @Autowired
    private AreaBusinessService areaBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return areaBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAll(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryAll(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByCodes(RequestJsonVO requestVo) {
        return areaBusinessService.findByCodes(requestVo);
    }

    @Override
    public ResultObjectVO queryAreaTreeTable(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return areaBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return areaBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return areaBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return areaBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByParentCode(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryListByParentCode(requestJsonVO);
    }

    @Override
    public ResultObjectVO flushAllCache(RequestJsonVO requestVo) {
        return areaBusinessService.flushAllCache(requestVo);
    }

    @Override
    public ResultObjectVO queryFullCache(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryFullCache(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTreeChildByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryCityListByNames(RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryCityListByNames(requestJsonVO);
    }
}
