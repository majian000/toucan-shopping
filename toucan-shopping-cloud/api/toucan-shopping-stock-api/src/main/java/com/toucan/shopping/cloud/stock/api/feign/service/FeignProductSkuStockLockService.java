package com.toucan.shopping.cloud.stock.api.feign.service;

import com.toucan.shopping.cloud.stock.api.feign.fallback.FeignProductSkuStockLockServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-stock-proxy/productSkuStockLock",fallbackFactory = FeignProductSkuStockLockServiceFallbackFactory.class)
public interface FeignProductSkuStockLockService {


    /**
     * 锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/lock/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO lockStock(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据SKUID查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByProductSkuIds(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStock(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据主订单编号查询锁定库存数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStockByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 根据子订单编号查询锁定库存数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num/by/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockNumByOrderNo(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock/by/orderNo",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteLockStockByOrderNo(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据主订单编号查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/list/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    ResultObjectVO findLockStockListByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO);

}
