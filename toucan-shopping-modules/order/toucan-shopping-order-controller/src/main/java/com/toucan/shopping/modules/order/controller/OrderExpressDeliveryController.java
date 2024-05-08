package com.toucan.shopping.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.enums.ExpressCompanyEnum;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.*;
import com.toucan.shopping.modules.order.vo.*;
import com.toucan.shopping.modules.pay.vo.PayCallbackVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单快递信息表
 * @author majian
 */
@RestController
@RequestMapping("/orderExpressDelivery")
public class OrderExpressDeliveryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private OrderExpressDeliveryService orderExpressDeliveryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLogService orderLogService;


    /**
     * 保存或修改
     */
    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            OrderExpressDeliveryVO orderExpressDeliveryVO =requestJsonVO.formatEntity(OrderExpressDeliveryVO.class);
            if(orderExpressDeliveryVO.getOrderId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单");
                return resultObjectVO;
            }

            try {
                OrderVO queryOrderVO = new OrderVO();
                queryOrderVO.setShopId(orderExpressDeliveryVO.getShopId());
                queryOrderVO.setId(orderExpressDeliveryVO.getOrderId());
                OrderVO orderVO = orderService.queryOneVOByVO(queryOrderVO);
                if(orderVO==null){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("没有找到订单");
                    return resultObjectVO;
                }
                if(orderVO.getTradeStatus().intValue()!=OrderConstant.TRADE_STATUS_NON_PAYMENT
                        &&orderVO.getTradeStatus().intValue()!=OrderConstant.TRADE_STATUS_WAIT_DELIVERY){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("订单状态已变更,不允许发货");
                    return resultObjectVO;
                }
                orderExpressDeliveryVO.setBuyerUserMainId(Long.parseLong(orderVO.getUserId()));
                OrderExpressDelivery orderExpressDelivery = orderExpressDeliveryService.queryByOrderId(orderExpressDeliveryVO.getOrderId());
                int ret = 0;
                if(orderExpressDelivery==null){
                    orderExpressDeliveryVO.setCreateDate(new Date());
                    orderExpressDeliveryVO.setShardingDate(orderExpressDeliveryVO.getCreateDate());
                    orderExpressDeliveryVO.setDeleteStatus((short)0);
                    ret = orderExpressDeliveryService.save(orderExpressDeliveryVO);
                    if(ret>0){
                        orderLogService.save(orderExpressDeliveryVO.getOperateUserId(),requestJsonVO.getAppCode(),orderVO.getOrderNo(),
                                "订单关联快递信息",null,orderExpressDeliveryVO,OrderConstant.ORDER_LOG_TYPE_EXPRESS_DELIVERY);
                        ret = orderService.updateTradeStatus(orderExpressDeliveryVO.getOrderId(),OrderConstant.TRADE_STATUS_WAIT_RECEIVER);
                        if(ret>0){
                            orderLogService.save(orderExpressDeliveryVO.getOperateUserId(),requestJsonVO.getAppCode(),orderVO.getOrderNo(),
                                    "修改订单交易状态",orderVO.getTradeStatus(),OrderConstant.TRADE_STATUS_WAIT_RECEIVER,OrderConstant.ORDER_LOG_TYPE_UPDATE_ORDER_TRADE_STATUS);
                        }
                    }
                }else{
                    orderExpressDelivery.setCourierNumber(orderExpressDeliveryVO.getCourierNumber());
                    orderExpressDelivery.setCompanyTypeCode(orderExpressDeliveryVO.getCompanyTypeCode());
                    orderExpressDelivery.setCompanyTypeName(orderExpressDeliveryVO.getCompanyTypeName());
                    orderExpressDelivery.setUpdateDate(new Date());
                    ret = orderExpressDeliveryService.update(orderExpressDelivery);
                }
                if(ret<=0) {
                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                    resultObjectVO.setMsg("操作失败");
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
            }
        }
        return resultObjectVO;
    }



    /**
     * 根据订单ID删除
     */
    @RequestMapping(value="/removeByOrderId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO removeByOrderId(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            OrderExpressDeliveryVO orderExpressDeliveryVO =requestJsonVO.formatEntity(OrderExpressDeliveryVO.class);
            if(orderExpressDeliveryVO.getOrderId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单");
                return resultObjectVO;
            }

            try {
                OrderVO queryOrderVO = new OrderVO();
                queryOrderVO.setShopId(orderExpressDeliveryVO.getShopId());
                queryOrderVO.setId(orderExpressDeliveryVO.getOrderId());
                OrderVO orderVO = orderService.queryOneVOByVO(queryOrderVO);
                if(orderVO==null){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("没有找到订单");
                    return resultObjectVO;
                }
                if(orderVO.getTradeStatus().intValue()==OrderConstant.TRADE_STATUS_CALCEL
                        ||orderVO.getTradeStatus().intValue()==OrderConstant.TRADE_STATUS_FINISH){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("订单状态已变更,不允许删除订单快递信息");
                    return resultObjectVO;
                }

                OrderExpressDelivery oldOrderExpressDelivery = orderExpressDeliveryService.queryByOrderId(orderExpressDeliveryVO.getOrderId());
                orderLogService.save(orderExpressDeliveryVO.getOperateUserId(),requestJsonVO.getAppCode(),orderVO.getOrderNo(),
                        "删除订单快递信息",null,oldOrderExpressDelivery,OrderConstant.ORDER_LOG_TYPE_DELETE_EXPRESS_DELIVERY);
                int ret = orderExpressDeliveryService.removeByOrderId(orderExpressDeliveryVO.getOrderId());
                if(ret>0) {
                    //改为待发货
                    ret = orderService.updateTradeStatus(orderExpressDeliveryVO.getOrderId(),OrderConstant.TRADE_STATUS_WAIT_DELIVERY);
                    if(ret>0) {
                        orderLogService.save(orderExpressDeliveryVO.getOperateUserId(),requestJsonVO.getAppCode(),orderVO.getOrderNo(),
                                "修改订单交易状态",orderVO.getTradeStatus(),OrderConstant.TRADE_STATUS_WAIT_DELIVERY,OrderConstant.ORDER_LOG_TYPE_UPDATE_ORDER_TRADE_STATUS);
                        resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                        resultObjectVO.setMsg("请求成功");
                    }
                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败");
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
            }
        }
        return resultObjectVO;
    }


    /**
     * 根据订单ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/findOneByOrderIdAndShopId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(@RequestBody RequestJsonVO requestJsonVO){

        ResultTypeObjectVO<OrderExpressDeliveryVO> resultObjectVO = new ResultTypeObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            OrderExpressDeliveryVO orderExpressDeliveryVO =requestJsonVO.formatEntity(OrderExpressDeliveryVO.class);
            if(orderExpressDeliveryVO.getOrderId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单ID");
                return resultObjectVO;
            }
            if(orderExpressDeliveryVO.getShopId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到店铺ID");
                return resultObjectVO;
            }
            try {
                OrderVO queryOrderVO = new OrderVO();
                queryOrderVO.setShopId(orderExpressDeliveryVO.getShopId());
                queryOrderVO.setId(orderExpressDeliveryVO.getOrderId());
                OrderVO orderVO = orderService.queryOneVOByVO(queryOrderVO);
                if(orderVO==null){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("没有找到订单");
                    return resultObjectVO;
                }
                orderExpressDeliveryVO = orderExpressDeliveryService.queryVOByOrderId(queryOrderVO.getId());
                if(orderExpressDeliveryVO!=null) {
                    orderExpressDeliveryVO.setCompanyTypeName(ExpressCompanyEnum.getByCode(orderExpressDeliveryVO.getCompanyTypeCode()).getName());
                }
                resultObjectVO.setData(orderExpressDeliveryVO);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败");
            }
        }
        return resultObjectVO;
    }


}
