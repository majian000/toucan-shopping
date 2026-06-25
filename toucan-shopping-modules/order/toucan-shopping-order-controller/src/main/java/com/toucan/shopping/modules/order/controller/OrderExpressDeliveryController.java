package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderExpressDeliveryBusinessService;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单快递信息表
 */
@RestController
@RequestMapping("/orderExpressDelivery")
public class OrderExpressDeliveryController {

    @Autowired
    private OrderExpressDeliveryBusinessService orderExpressDeliveryBusinessService;

    /**
     * 保存或修改
     */
    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.saveOrUpdate(requestJsonVO);
    }

    /**
     * 根据订单ID删除
     */
    @RequestMapping(value="/removeByOrderId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO removeByOrderId(@RequestBody RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.removeByOrderId(requestJsonVO);
    }

    /**
     * 根据订单ID和店铺ID查询
     */
    @RequestMapping(value="/findOneByOrderIdAndShopId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(@RequestBody RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.findOneByOrderIdAndShopId(requestJsonVO);
    }
}
