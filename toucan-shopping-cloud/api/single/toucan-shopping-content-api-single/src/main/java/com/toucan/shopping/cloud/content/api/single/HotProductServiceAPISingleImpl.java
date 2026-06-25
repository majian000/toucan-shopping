package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.HotProductServiceAPI;
import com.toucan.shopping.modules.column.business.service.HotProductBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotProductServiceAPISingleImpl implements HotProductServiceAPI {

    @Autowired
    private HotProductBusinessService hotProductBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return hotProductBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryPcIndexHotProducts(RequestJsonVO requestVo) {
        return hotProductBusinessService.queryPcIndexHotProducts(requestVo);
    }

}
