package com.toucan.shopping.cloud.stock.api.single;

import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.stock.business.service.ProductSkuStockLockBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuStockLockServiceSingleImpl implements FeignProductSkuStockLockService {

    @Autowired
    private ProductSkuStockLockBusinessService productSkuStockLockBusinessService;

    @Override
    public ResultObjectVO lockStock(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.lockStock(requestJsonVO);
    }

    @Override
    public ResultObjectVO findLockStockNumByProductSkuIds(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.findLockStockNumByProductSkuIds(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteLockStock(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.deleteLockStock(requestJsonVO);
    }

    @Override
    public ResultObjectVO findLockStockNumByMainOrderNos(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.findLockStockNumByMainOrderNos(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteLockStockByMainOrderNos(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.deleteLockStockByMainOrderNos(requestJsonVO);
    }

    @Override
    public ResultObjectVO findLockStockNumByOrderNo(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.findLockStockNumByOrderNo(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteLockStockByOrderNo(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.deleteLockStockByOrderNo(requestJsonVO);
    }

    @Override
    public ResultObjectVO findLockStockListByMainOrderNos(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.findLockStockListByMainOrderNos(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        return productSkuStockLockBusinessService.findById(requestJsonVO);
    }
}
