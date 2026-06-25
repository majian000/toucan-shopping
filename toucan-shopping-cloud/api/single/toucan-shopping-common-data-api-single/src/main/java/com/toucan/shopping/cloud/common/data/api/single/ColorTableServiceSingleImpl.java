package com.toucan.shopping.cloud.common.data.api.single;

import com.toucan.shopping.cloud.common.data.api.ColorTableServiceAPI;
import com.toucan.shopping.modules.color.table.business.service.ColorTableBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorTableServiceSingleImpl implements ColorTableServiceAPI {

    @Autowired
    private ColorTableBusinessService colorTableBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByNames(RequestJsonVO requestJsonVO) {
        return colorTableBusinessService.queryListByNames(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return colorTableBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return colorTableBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return colorTableBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return colorTableBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return colorTableBusinessService.deleteByIds(requestVo);
    }
}
