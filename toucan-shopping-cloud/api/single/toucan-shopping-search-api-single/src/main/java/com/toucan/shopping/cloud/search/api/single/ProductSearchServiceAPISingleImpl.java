package com.toucan.shopping.cloud.search.api.single;

import com.toucan.shopping.cloud.search.api.ProductSearchServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.search.business.service.ProductSearchBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchServiceAPISingleImpl implements ProductSearchServiceAPI {

    @Autowired
    private ProductSearchBusinessService productSearchBusinessService;

    @Override
    public ResultObjectVO search(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.search(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryBySkuId(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.queryBySkuId(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO removeById(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.removeById(requestJsonVO);
    }

    @Override
    public ResultObjectVO clear(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.clear(requestJsonVO);
    }

    @Override
    public ResultObjectVO count(RequestJsonVO requestJsonVO) {
        return productSearchBusinessService.count(requestJsonVO);
    }

}
