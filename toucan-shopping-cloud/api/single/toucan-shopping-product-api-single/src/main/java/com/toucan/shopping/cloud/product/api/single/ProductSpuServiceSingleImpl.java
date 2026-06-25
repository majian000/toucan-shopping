package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.ProductSpuServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ProductSpuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSpuServiceSingleImpl implements ProductSpuServiceAPI {

    @Autowired
    private ProductSpuBusinessService productSpuBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return productSpuBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return productSpuBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return productSpuBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return productSpuBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return productSpuBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return productSpuBusinessService.findById(requestVo);
    }
}
