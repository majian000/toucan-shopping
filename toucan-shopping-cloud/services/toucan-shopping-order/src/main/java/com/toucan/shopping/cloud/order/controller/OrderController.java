package com.toucan.shopping.cloud.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.QueryOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private OrderNoService orderNoService;


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
                order.setCreateUserId("-1L");
                order.setDeleteStatus((short) 0);
                orderService.create(order);
            }
        }
        return resultObjectVO;
    }


    /**
     * 创建订单
     */
    @RequestMapping(value="/create",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO create(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            CreateOrderVO createOrderVo = JSON.parseObject(requestJsonVO.getEntityJson(), CreateOrderVO.class);
//            if(createOrderVo.getUserId()==null)
//            {
//                resultObjectVO.setCode(ResultObjectVO.FAILD);
//                resultObjectVO.setMsg("没有找到用户");
//                return resultObjectVO;
//            }
//            String orderNo=createOrderVo.getOrderNo();
//            String userId=createOrderVo.getUserId();
//            String appCode=createOrderVo.getAppCode();
//            Integer payMethod = createOrderVo.getPayMethod();

            try {

                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("订单创建完成");
            }catch(Exception e)
            {
//                orderService.deleteByOrderNo(orderNo);
//                orderItemService.deleteByOrderNo(orderNo);

                logger.warn(e.getMessage(),e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("订单创建失败");
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
                List<OrderItem> orderItems = orderItemService.findByOrderNo(orderNo,userId);
                List<ProductSku> productSkus = new ArrayList<ProductSku>();
                if(!CollectionUtils.isEmpty(orderItems))
                {
                    for(OrderItem orderItem:orderItems)
                    {
                        ProductSku productSku = new ProductSku();
                        productSku.setUuid(orderItem.getSkuUuid());
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
                Order order = JSONObject.parseObject(requestJsonVO.getEntityJson(),Order.class);
                int row = orderService.finishOrder(order);
                if(row<1)
                {
                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                    resultObjectVO.setMsg("请求失败");
                    return resultObjectVO;
                }

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
     * 取消订单
     */
    @RequestMapping(value="/cancel",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO cancel(@RequestBody RequestJsonVO requestJsonVO){

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
                logger.info("取消订单 params {}",requestJsonVO.getEntityJson());
                Order order = JSONObject.parseObject(requestJsonVO.getEntityJson(),Order.class);
                int row = orderService.cancelOrder(order);
                if(row<1)
                {
                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                    resultObjectVO.setMsg("请求失败");
                    return resultObjectVO;
                }

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

}
