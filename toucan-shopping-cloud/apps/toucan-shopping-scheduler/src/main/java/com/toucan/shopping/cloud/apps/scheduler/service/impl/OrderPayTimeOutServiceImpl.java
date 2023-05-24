package com.toucan.shopping.cloud.apps.scheduler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.constant.PublishEventConstant;
import com.toucan.shopping.cloud.apps.scheduler.service.OrderPayTimeOutService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class OrderPayTimeOutServiceImpl implements OrderPayTimeOutService {


    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private EventPublishService eventProcessService;

    /**
     * 保存取消的主订单事件
     */
    public EventPublish saveEvent(String globalTransactionId, Object payload, String remark, String type)
    {
        //取消订单对象
        EventPublish eventProcess = new EventPublish();
        eventProcess.setId(idGenerator.id());
        eventProcess.setCreateDate(new Date());
        eventProcess.setBusinessId("");
        eventProcess.setRemark(remark);
        eventProcess.setTransactionId(globalTransactionId);
        eventProcess.setPayload(JSONObject.toJSONString(payload));
        eventProcess.setStatus((short) 0); //待处理
        eventProcess.setType(type);
        eventProcessService.insert(eventProcess);

        return eventProcess;
    }


    /**
     * 完成事件
     */
    public void finishEvent(EventPublish eventProcess)
    {
        if(eventProcess!=null) {
            eventProcess.setStatus((short) 1); //已处理
            eventProcessService.updateStatus(eventProcess);
        }
    }

    @Override
    public ResultObjectVO restoreStock(String globalTransactionId,EventPublish eventProcess,List<ProductSkuStockLockVO> productSkuStockLocks,ProductSkuStockLockVO productSkuStockLockVO) throws Exception {
        if(!CollectionUtils.isEmpty(productSkuStockLocks))
        {
            List<InventoryReductionVO> inventoryReductions= new LinkedList<>();
            for(ProductSkuStockLockVO pssl:productSkuStockLocks)
            {
                InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                inventoryReductionVO.setProductSkuId(pssl.getProductSkuId());
                inventoryReductionVO.setStockNum(pssl.getStockNum());
                inventoryReductions.add(inventoryReductionVO);
            }
            if(!CollectionUtils.isEmpty(inventoryReductions)) {
                //保存还原锁定库存事件
                Map<String,Object> params = new HashMap();
                params.put("inventoryReductions",inventoryReductions);
                params.put("productSkuStockLock",productSkuStockLockVO);
                eventProcess = this.saveEvent(globalTransactionId,params,"还原库存", PublishEventConstant.restart_product_lock_stock_num.name());
                return  feignProductSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
            }
        }
        return new ResultObjectVO(ResultObjectVO.FAILD,"");
    }

    @Override
    public ResultObjectVO deleteLockStockByMainOrderNos(String globalTransactionId, EventPublish eventProcess, ProductSkuStockLockVO productSkuStockLockVO) throws Exception {
        //保存删除锁定库存事件
        eventProcess = this.saveEvent(globalTransactionId,productSkuStockLockVO,"删除锁定库存",PublishEventConstant.delete_lock_stock_num.name());
        return feignProductSkuStockLockService.deleteLockStockByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
    }

}
