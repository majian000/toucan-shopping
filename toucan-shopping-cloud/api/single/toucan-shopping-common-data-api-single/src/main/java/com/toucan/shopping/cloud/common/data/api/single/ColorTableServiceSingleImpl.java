package com.toucan.shopping.cloud.common.data.api.single;

import com.toucan.shopping.cloud.common.data.api.feign.service.FeignColorTableService;
import com.toucan.shopping.modules.color.table.business.service.ColorTableBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorTableServiceSingleImpl implements FeignColorTableService {

    @Autowired
    private ColorTableBusinessService colorTableBusinessService;

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(String signHeader, RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByNames(RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryListByNames(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return colorTableBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return colorTableBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return colorTableBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return colorTableBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return colorTableBusinessService.deleteByIds(requestVo);
    }
}
