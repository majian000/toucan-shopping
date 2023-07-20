package com.toucan.shopping.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.vo.*;
import com.toucan.shopping.modules.pay.vo.PayCallbackVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private MainOrderService mainOrderService;

    @Autowired
    private OrderNoService orderNoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderConsigneeAddressService orderConsigneeAddressService;

    @Autowired
    private OrderLogService orderLogService;

    /**
     * 测试分片
     */
    @RequestMapping(value="/testSharding",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO testSharding(@RequestBody RequestJsonVO requestJsonVO) throws Exception{
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        for(int i=2021;i<=2023;i++) {
            for(int j=1;j<=12;j++) {
                String dateString=i+"-"+j+"-01 00:01:00";
                if(j<10)
                {
                    dateString=i+"-0"+j+"-01 00:01:00";
                }
                Order order = new Order();
                order.setId(idGenerator.id());
                order.setCreateDate(DateUtils.parse(dateString,DateUtils.FORMATTER_SS.get()));
                order.setOrderNo(orderNoService.generateOrderNo());
                order.setUserId("-1");
                order.setOrderAmount(new BigDecimal(0.0D));
                order.setPayAmount(new BigDecimal(0.0D));
                order.setPayStatus(0);
                order.setTradeStatus(0);
                order.setTotalAmount(new BigDecimal(0.0D));
                order.setPayType(0);
                order.setDeleteStatus((short) 0);
                orderService.create(order);
            }
        }
        return resultObjectVO;
    }




    /**
     * 取消订单
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            OrderVO orderVO = requestJsonVO.formatEntity(OrderVO.class);
            if(StringUtils.isEmpty(orderVO.getOrderNo()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单编号");
                return resultObjectVO;
            }

            try {

                orderService.cancelOrderByOrderNo(orderVO.getOrderNo(),orderVO.getCancelRemark());

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
     * 根据订单编号查询所有skuid
     */
    @RequestMapping(value="/querySkuUuids/orderNo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO querySkuUuidsByOrderNo(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            QueryOrderVo queryOrderVo = JSON.parseObject(requestJsonVO.getEntityJson(), QueryOrderVo.class);
            if(queryOrderVo.getUserId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户");
                return resultObjectVO;
            }
            String orderNo=queryOrderVo.getOrderNo();
            String userId=queryOrderVo.getUserId();

            try {
                List<OrderItemVO> orderItems = orderItemService.findByOrderNo(orderNo,userId);
                List<ProductSku> productSkus = new ArrayList<ProductSku>();
                if(!CollectionUtils.isEmpty(orderItems))
                {
                    for(OrderItem orderItem:orderItems)
                    {
                        ProductSku productSku = new ProductSku();
                        productSku.setId(orderItem.getSkuId());
                        productSkus.add(productSku);
                    }
                }
                resultObjectVO.setData(productSkus);
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("请求完成");
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
     * 查询支付超时订单
     */
    @RequestMapping(value="/query/pay/timeout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderByPayTimeOut(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                Order order = JSONObject.parseObject(requestJsonVO.getEntityJson(),Order.class);
                order.setAppCode(requestJsonVO.getAppCode());
                resultObjectVO.setData(orderService.queryOrderListByPayTimeout(order));
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("请求完成");
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
     * 更新订单
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                OrderVO orderVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),OrderVO.class);
                Order oldOrder = orderService.findById(orderVO.getId());
                OrderLog orderLog = new OrderLog();
                orderLog.setOperateUserId(orderVO.getOperateUserId());
                orderLog.setAppCode(requestJsonVO.getAppCode());
                orderLog.setId(idGenerator.id());
                orderLog.setCreateDate(new Date());
                orderLog.setOrderNo(oldOrder.getOrderNo());
                orderLog.setRemark("修改订单信息");
                orderLog.loadOldData(oldOrder).loadUpdateData(orderVO).setDataBodyType(1).loadDataBody();
                orderLogService.save(orderLog);


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
     * 查询支付超时订单页
     */
    @RequestMapping(value="/query/pay/timeout/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderByPayTimeOutPage(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                OrderPageInfo orderPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),OrderPageInfo.class);
                orderPageInfo.setAppCode(requestJsonVO.getAppCode());
                resultObjectVO.setData(orderService.queryOrderListByPayTimeoutPage(orderPageInfo));
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
         * 完成订单
         */
    @RequestMapping(value="/finish",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO finish(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            QueryOrderVo queryOrderVo = JSON.parseObject(requestJsonVO.getEntityJson(), QueryOrderVo.class);
            if(queryOrderVo.getUserId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户");
                return resultObjectVO;
            }

            try {
                logger.info("完成订单 params {}",requestJsonVO.getEntityJson());
                PayCallbackVO payCallbackVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),PayCallbackVO.class);
//                int row = orderService.finishOrder(order);
//                if(row<1)
//                {
//                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
//                    resultObjectVO.setMsg("请求失败");
//                    return resultObjectVO;
//                }

                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("请求完成");
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
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                OrderPageInfo orderPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),OrderPageInfo.class);
                PageInfo<OrderVO> orderPage = orderService.queryOrderListPage(orderPageInfo);
                if(!CollectionUtils.isEmpty(orderPage.getList()))
                {
                    //查询所有订单项
                    List<String> orderNos = orderPage.getList().stream().map(OrderVO::getOrderNo).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(orderNos))
                    {
                        List<OrderItemVO> orderItems = orderItemService.findByOrderNos(orderNos);
                        List<OrderConsigneeAddressVO> orderConsigneeAddresss= orderConsigneeAddressService.queryListByOrderNos(orderNos);
                        for(OrderVO orderVO:orderPage.getList())
                        {
                            orderVO.setOrderItems(new LinkedList<>());
                            for(OrderItemVO orderItemVO:orderItems)
                            {
                                if(orderItemVO.getOrderNo().equals(orderVO.getOrderNo()))
                                {
                                    orderVO.getOrderItems().add(orderItemVO);
                                }
                            }

                            for(OrderConsigneeAddressVO consigneeAddressVO:orderConsigneeAddresss)
                            {
                                if(consigneeAddressVO.getOrderNo().equals(orderVO.getOrderNo()))
                                {
                                    consigneeAddressVO.setPhone(PhoneUtils.desensitization(consigneeAddressVO.getPhone()));
                                    orderVO.setOrderConsigneeAddress(consigneeAddressVO);
                                }
                            }
                        }
                    }

                }
                resultObjectVO.setData(orderPage);
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("请求完成");
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
     * 查询子订单
     */
    @RequestMapping(value="/queryByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                OrderVO orderVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),OrderVO.class);
                resultObjectVO.setData(orderService.queryOneByVO(orderVO));
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("请求完成");
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
     * 根据ID查询
     */
    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            OrderVO orderVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),OrderVO.class);
            orderVO = orderService.queryOneVOByVO(orderVO);
            orderVO.setOrderConsigneeAddress(orderConsigneeAddressService.queryOneByOrderNo(orderVO.getOrderNo()));
            resultObjectVO.setData(orderVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }


}
