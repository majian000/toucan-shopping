package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.product.business.service.ProductSkuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuServiceSingleImpl implements ProductSkuServiceAPI {

    @Autowired
    private ProductSkuBusinessService productSkuBusinessService;

    @Override
    public ResultListVO queryShelvesList(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryShelvesList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryByIdList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryByIdForFront(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateStock(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updateStock(requestJsonVO);
    }

    @Override
    public ResultObjectVO updatePrice(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updatePrice(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneByShopProductIdForFrontPreview(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryOneByShopProductIdForFrontPreview(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdForFrontPreview(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryByIdForFrontPreview(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneByShopProductIdForFront(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryOneByShopProductIdForFront(requestJsonVO);
    }

    @Override
    public ResultObjectVO shelves(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.shelves(requestJsonVO);
    }

    @Override
    public ResultObjectVO inventoryReduction(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.inventoryReduction(requestJsonVO);
    }

    @Override
    public ResultObjectVO restoreStock(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.restoreStock(requestJsonVO);
    }

    @Override
    public ResultObjectVO updatePreviewPhoto(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updatePreviewPhoto(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateDescriptionPhoto(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.updateDescriptionPhoto(requestJsonVO);
    }

    @Override
    public ResultObjectVO removeDescriptionPhoto(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.removeDescriptionPhoto(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByShopProductIdList(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryListByShopProductIdList(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<Long> queryShelvesCountByShopId(RequestJsonVO requestJsonVO) {
        return productSkuBusinessService.queryShelvesCountByShopId(requestJsonVO);
    }
}
