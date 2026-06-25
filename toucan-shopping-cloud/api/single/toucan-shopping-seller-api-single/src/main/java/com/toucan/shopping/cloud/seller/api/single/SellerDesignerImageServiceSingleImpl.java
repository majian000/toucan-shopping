package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerImageService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerDesignerImageBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerDesignerImageServiceSingleImpl implements FeignSellerDesignerImageService {

    @Autowired
    private SellerDesignerImageBusinessService sellerDesignerImageBusinessService;

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return sellerDesignerImageBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        return sellerDesignerImageBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

}
