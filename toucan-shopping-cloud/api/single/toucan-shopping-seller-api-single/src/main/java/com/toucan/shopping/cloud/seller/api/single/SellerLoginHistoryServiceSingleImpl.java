package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.SellerLoginHistoryServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerLoginHistoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerLoginHistoryServiceSingleImpl implements SellerLoginHistoryServiceAPI {

    @Autowired
    private SellerLoginHistoryBusinessService sellerLoginHistoryBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return sellerLoginHistoryBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return sellerLoginHistoryBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO queryListByLatest10(RequestJsonVO requestVo) {
        return sellerLoginHistoryBusinessService.queryListByLatest10(requestVo);
    }

}
