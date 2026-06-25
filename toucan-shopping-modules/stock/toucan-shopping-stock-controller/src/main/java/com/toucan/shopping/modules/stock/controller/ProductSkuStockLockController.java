package com.toucan.shopping.modules.stock.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.stock.business.service.ProductSkuStockLockBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productSkuStockLock")
public class ProductSkuStockLockController {

    @Autowired
    private ProductSkuStockLockBusinessService productSkuStockLockBusinessService;


    /**
     * 锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/lock/stock",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO lockStock(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.lockStock(requestJsonVO);
    }



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.queryListPage(requestJsonVO);
    }




    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/by/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.findById(requestJsonVO);
    }

    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteLockStock(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.deleteLockStock(requestJsonVO);
    }

    /**
     * 根据SKUID查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockNumByProductSkuIds(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.findLockStockNumByProductSkuIds(requestJsonVO);
    }


    /**
     * 根据主订单编号查询锁定库存数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockNumByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.findLockStockNumByMainOrderNos(requestJsonVO);
    }

    /**
     * 根据主订单编号查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/list/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockListByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.findLockStockListByMainOrderNos(requestJsonVO);
    }

    /**
     * 根据子订单编号查询锁定库存数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num/by/orderNo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockNumByOrderNo(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.findLockStockNumByOrderNo(requestJsonVO);
    }

    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock/by/orderNo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteLockStockByOrderNo(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.deleteLockStockByOrderNo(requestJsonVO);
    }


    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteLockStockByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSkuStockLockBusinessService.deleteLockStockByMainOrderNos(requestJsonVO);
    }
}
