package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerDesignerPageModelBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerDesignerPageModelServiceSingleImpl implements FeignSellerDesignerPageModelService {

    @Autowired
    private SellerDesignerPageModelBusinessService sellerDesignerPageModelBusinessService;

    @Override
    public ResultObjectVO onlySaveOne(RequestJsonVO requestJsonVO) {
        return sellerDesignerPageModelBusinessService.onlySaveOne(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryLastOne(RequestJsonVO requestJsonVO) {
        return sellerDesignerPageModelBusinessService.queryLastOne(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return sellerDesignerPageModelBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        return sellerDesignerPageModelBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

}
