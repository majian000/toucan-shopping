package com.toucan.shopping.modules.order.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemBusinessService {

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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAllListByOrderId(RequestJsonVO requestJsonVO) {
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updatesFromOrderList(RequestJsonVO requestJsonVO) {
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
