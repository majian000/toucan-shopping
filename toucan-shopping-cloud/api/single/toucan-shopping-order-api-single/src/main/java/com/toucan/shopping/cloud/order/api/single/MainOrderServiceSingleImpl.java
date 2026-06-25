package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.MainOrderServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.MainOrderBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainOrderServiceSingleImpl implements MainOrderServiceAPI {

    @Autowired
    private MainOrderBusinessService mainOrderBusinessService;

    @Override
    public ResultObjectVO create(RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.create(requestJsonVO);
    }

    @Override
    public ResultObjectVO cancel(RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.cancel(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOrderByPayTimeOut(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryMainOrderByOrderNoAndUserId(RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.queryMainOrderByOrderNoAndUserId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOrderByPayTimeOutPage(RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.queryOrderByPayTimeOutPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO batchCancelPayTimeout(RequestJsonVO requestJsonVO) {
        return mainOrderBusinessService.batchCancelPayTimeout(requestJsonVO);
    }
}
