package com.toucan.shopping.cloud.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.service.MainOrderService;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
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
@RequestMapping("/main/order")
public class MainOrderController {

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
     * 创建订单
     */
    @RequestMapping(value="/create",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO create(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("订单创建失败");
            return resultObjectVO;
        }
        logger.info("创建订单 {} ",requestJsonVO.getEntityJson());
        CreateOrderVO createOrder = JSON.parseObject(requestJsonVO.getEntityJson(), CreateOrderVO.class);
        try {
            if(createOrder.getMainOrder()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("主订单不能为空");
                return resultObjectVO;
            }
            if(CollectionUtils.isEmpty(createOrder.getMainOrder().getOrders()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("子订单不能为空");
                return resultObjectVO;
            }
            for(OrderVO orderVO:createOrder.getMainOrder().getOrders())
            {
                if(CollectionUtils.isEmpty(orderVO.getBuyCarItems()))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("子订单项不能为空");
                    return resultObjectVO;
                }
            }
            mainOrderService.createOrder(createOrder.getMainOrder());
            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
            resultObjectVO.setMsg("订单创建完成");
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("订单创建失败");
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

            MainOrderVO mainOrderVO = JSON.parseObject(requestJsonVO.getEntityJson(), MainOrderVO.class);
            if(mainOrderVO.getUserId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(mainOrderVO.getOrderNo()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单编号");
                return resultObjectVO;
            }

            try {
                logger.info("取消订单 params {}",requestJsonVO.getEntityJson());
                int row = mainOrderService.cancelMainOrder(mainOrderVO.getOrderNo(),mainOrderVO.getUserId());
                if(row<1)
                {
                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                    resultObjectVO.setMsg("取消主订单失败");
                    return resultObjectVO;
                }

                orderService.cancelNoPayOrderByMainOrderNo(mainOrderVO.getOrderNo(),mainOrderVO.getUserId());

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
     * 查询主订单
     */
    @RequestMapping(value="/queryMainOrderByOrderNoAndUserId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryMainOrderByOrderNoAndUserId(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            try {
                MainOrderVO mainOrderVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),MainOrderVO.class);
                resultObjectVO.setData(mainOrderService.queryOneByVO(mainOrderVO));
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
