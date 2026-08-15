package com.toucan.shopping.modules.order.business.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.page.MainOrderPageInfo;
import com.toucan.shopping.modules.order.service.MainOrderService;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MainOrderBusinessService {

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
    private OrderLogService orderLogService;

    /**
     * 测试分片
     */
    @RequestCheck
    public ResultObjectVO testSharding(RequestJsonVO requestJsonVO) throws Exception {
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
                order.setPayStatus(OrderConstant.PAY_STATUS_NON_PAYMENT);
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO create(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        logger.info("创建订单 {} ",requestJsonVO.getEntityJson());
        CreateOrderVO createOrder = JSON.parseObject(requestJsonVO.getEntityJson(), CreateOrderVO.class);
        try {
            Check.notNull(createOrder.getMainOrder(), ResultObjectVO.FAILD, "主订单不能为空");
            Check.notEmpty(createOrder.getMainOrder().getOrders(), ResultObjectVO.FAILD, "子订单不能为空");
            for(OrderVO orderVO:createOrder.getMainOrder().getOrders())
            {
                Check.notEmpty(orderVO.getBuyCarItems(), ResultObjectVO.FAILD, "子订单项不能为空");
            }
            mainOrderService.createOrder(createOrder);
            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
            resultObjectVO.setMsg("订单创建完成");
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "订单创建失败");
        }
        return resultObjectVO;
    }

    /**
     * 取消订单
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO cancel(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            MainOrderVO mainOrderVO = JSON.parseObject(requestJsonVO.getEntityJson(), MainOrderVO.class);
            Check.notNull(mainOrderVO.getUserId(), ResultObjectVO.FAILD, "没有找到用户");
            Check.notEmpty(mainOrderVO.getOrderNo(), ResultObjectVO.FAILD, "没有找到订单编号");
            logger.info("取消订单 params {}",requestJsonVO.getEntityJson());
            int row = mainOrderService.cancelMainOrder(mainOrderVO.getOrderNo(),mainOrderVO.getUserId(),"手动取消订单");
            if(row<1)
            {
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("取消主订单失败");
                return resultObjectVO;
            }

            List<Order> orderList = orderService.findListByMainOrderNo(mainOrderVO.getOrderNo());

            if(!CollectionUtils.isEmpty(orderList)) {
                String logBatchId = GlobalUUID.uuid();
                for(Order order:orderList) {
                    orderLogService.save(logBatchId, mainOrderVO.getUserId(), requestJsonVO.getAppCode(), order.getOrderNo(),
                            "手动取消订单", null,null, OrderConstant.ORDER_LOG_TYPE_CANCEL_ORDER);
                }
            }

            orderService.cancelNoPayOrderByMainOrderNo(mainOrderVO.getOrderNo(),mainOrderVO.getUserId(),"手动取消订单");

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求失败");
        }
        return resultObjectVO;
    }

    /**
     * 查询主订单
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryMainOrderByOrderNoAndUserId(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        try {
            MainOrderVO mainOrderVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),MainOrderVO.class);
            resultObjectVO.setData(mainOrderService.queryOneByVO(mainOrderVO));
            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
            resultObjectVO.setMsg("请求完成");
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求失败");
        }
        return resultObjectVO;
    }

    /**
     * 查询支付超时订单页
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOrderByPayTimeOutPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MainOrderPageInfo pageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),MainOrderPageInfo.class);
            pageInfo.setAppCode(requestJsonVO.getAppCode());
            resultObjectVO.setData(mainOrderService.queryMainOrderListByPayTimeoutPage(pageInfo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求失败");
        }
        return resultObjectVO;
    }

    /**
     * 批量取消支付超时订单
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO batchCancelPayTimeout(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MainOrderPageInfo pageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),MainOrderPageInfo.class);
            pageInfo.setAppCode(requestJsonVO.getAppCode());
            pageInfo.setSystemDate(new Date());
            PageInfo<MainOrderVO> pageResult =  mainOrderService.queryMainOrderListByPayTimeoutPage(pageInfo);
            List<MainOrderVO> mainOrders = pageResult.getList();
            String cancelRemark = "支付超时,自动取消订单";
            if(!CollectionUtils.isEmpty(mainOrders)) {
                for(MainOrderVO mainOrderVO:mainOrders) {
                    //取消订单和子订单
                    mainOrderService.cancelMainOrderAndOrders(mainOrderVO.getOrderNo(),mainOrderVO.getUserId(),mainOrderVO.getAppCode(),cancelRemark,mainOrderVO.getShardingDate());
                }
            }
            resultObjectVO.setData(pageResult);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求失败");
        }
        return resultObjectVO;
    }
}
