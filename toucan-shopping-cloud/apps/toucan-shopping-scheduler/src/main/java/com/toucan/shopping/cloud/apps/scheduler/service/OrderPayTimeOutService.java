package com.toucan.shopping.cloud.apps.scheduler.service;

import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;

import java.util.List;

public interface OrderPayTimeOutService {

    /**
     * 还原库存
     * @param globalTransactionId
     * @param productSkuStockLocks
     * @return
     * @throws Exception
     */
    ResultObjectVO restoreStock(String globalTransactionId,EventPublish eventProcess,List<ProductSkuStockLockVO> productSkuStockLocks,ProductSkuStockLockVO productSkuStockLockVO) throws Exception;

    /**
     * 根据主订单编号删除锁定库存
     * @param globalTransactionId
     * @param eventProcess
     * @param productSkuStockLockVO
     * @return
     */
    ResultObjectVO deleteLockStockByMainOrderNos(String globalTransactionId,EventPublish eventProcess,ProductSkuStockLockVO productSkuStockLockVO) throws Exception;

    EventPublish saveEvent(String globalTransactionId, Object payload, String remark, String type);

    void finishEvent(EventPublish eventProcess);

}
