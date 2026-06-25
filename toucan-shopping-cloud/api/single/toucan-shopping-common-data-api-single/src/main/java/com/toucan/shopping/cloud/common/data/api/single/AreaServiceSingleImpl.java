package com.toucan.shopping.cloud.common.data.api.single;

import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.business.service.AreaBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceSingleImpl implements FeignAreaService {

    @Autowired
    private AreaBusinessService areaBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAll(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryAll(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByCodes(RequestJsonVO requestVo) {
        return areaBusinessService.findByCodes(requestVo);
    }

    @Override
    public ResultObjectVO queryAreaTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return areaBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryTree(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return areaBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return areaBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return areaBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryListByPid(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByParentCode(String signHeader, RequestJsonVO requestJsonVO) {
        return areaBusinessService.queryListByParentCode(requestJsonVO);
    }

    @Override
    public ResultObjectVO flushAllCache(String signHeader, RequestJsonVO requestVo) {
        return areaBusinessService.flushAllCache(requestVo);
    }

    @Override
    public ResultObjectVO queryFullCache(String signHeader, RequestJsonVO requestJsonVO) {
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
