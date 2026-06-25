package com.toucan.shopping.cloud.stock.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ProductSkuStockLockServiceAPI {

    /**
     * 锁定库存
     */
    ResultObjectVO lockStock(RequestJsonVO requestJsonVO);

    /**
     * 根据SKUID查询锁定库存
     */
    ResultObjectVO findLockStockNumByProductSkuIds(RequestJsonVO requestJsonVO);

    /**
     * 删除锁定库存
     */
    ResultObjectVO deleteLockStock(RequestJsonVO requestJsonVO);

    /**
     * 根据主订单编号查询锁定库存数量
     */
    ResultObjectVO findLockStockNumByMainOrderNos(RequestJsonVO requestJsonVO);

    /**
     * 删除锁定库存
     */
    ResultObjectVO deleteLockStockByMainOrderNos(RequestJsonVO requestJsonVO);

    /**
     * 根据子订单编号查询锁定库存数量
     */
    ResultObjectVO findLockStockNumByOrderNo(RequestJsonVO requestJsonVO);

    /**
     * 删除锁定库存
     */
    ResultObjectVO deleteLockStockByOrderNo(RequestJsonVO requestJsonVO);

    /**
     * 根据主订单编号查询锁定库存
     */
    ResultObjectVO findLockStockListByMainOrderNos(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     */
    ResultObjectVO findById(RequestJsonVO requestJsonVO);

}
