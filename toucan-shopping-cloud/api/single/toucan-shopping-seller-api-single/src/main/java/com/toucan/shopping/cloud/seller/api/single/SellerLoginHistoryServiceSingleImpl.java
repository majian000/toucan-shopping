package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerLoginHistoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerLoginHistoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerLoginHistoryServiceSingleImpl implements FeignSellerLoginHistoryService {

    @Autowired
    private SellerLoginHistoryBusinessService sellerLoginHistoryBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerLoginHistoryBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        return sellerLoginHistoryBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO queryListByLatest10(String signHeader, RequestJsonVO requestVo) {
        return sellerLoginHistoryBusinessService.queryListByLatest10(requestVo);
    }

}
