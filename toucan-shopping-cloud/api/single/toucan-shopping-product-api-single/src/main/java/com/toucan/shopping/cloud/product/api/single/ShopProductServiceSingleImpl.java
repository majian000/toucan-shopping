package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.ShopProductServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopProductServiceSingleImpl implements ShopProductServiceAPI {

    @Autowired
    private ShopProductBusinessService shopProductBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByShopProductUuid(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryListByShopProductUuid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByShopProductId(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryByShopProductId(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO shelves(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.shelves(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneByFreightTemplateId(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.queryOneByFreightTemplateId(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateFreightTemplate(RequestJsonVO requestJsonVO) {
        return shopProductBusinessService.updateFreightTemplate(requestJsonVO);
    }
}
