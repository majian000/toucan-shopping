package com.toucan.shopping.modules.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.*;
import com.toucan.shopping.modules.order.vo.OrderConsigneeAddressVO;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.order.vo.QueryOrderVo;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order/orderItem")
public class OrderItemController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderLogService orderLogService;


    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {
            try {
                OrderItemPageInfo orderPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderItemPageInfo.class);
                PageInfo<OrderItemVO> orderItemPage = orderItemService.queryOrderListPage(orderPageInfo);
                resultObjectVO.setData(orderItemPage);
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
    @RequestMapping(value="/queryAllListByOrderId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAllListByOrderId(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {
            try {
                OrderItemVO orderItemVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderItemVO.class);
                resultObjectVO.setData(orderItemService.findByOrderId(orderItemVO.getOrderId()));
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
     * 修改订单项(从订单列表)
     */
    @RequestMapping(value="/updatesFromOrderList",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updatesFromOrderList(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        List<OrderItemVO> orderItems = requestJsonVO.formatEntityList(OrderItemVO.class);
        if(CollectionUtils.isNotEmpty(orderItems))
        {
            String logBatchId = GlobalUUID.uuid();
            for(OrderItemVO orderItemVO:orderItems)
            {
                orderItemService.updatesFromOrderList(orderItemVO);
                orderLogService.save(logBatchId,orderItemVO.getOperateUserId(),requestJsonVO.getAppCode(),orderItemVO.getOrderNo(),
                        "修改订单项信息",orderItemService.findById(orderItemVO.getId()),
                        orderItemVO, OrderConstant.ORDER_LOG_TYPE_ORDER_ITEMS);
            }
        }
        return resultObjectVO;
    }


}
