package com.toucan.shopping.modules.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.page.OrderLogPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
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
@RequestMapping("/order/orderLog")
public class OrderLogController {

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
                OrderLogPageInfo orderLogPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderLogPageInfo.class);
                if(StringUtils.isEmpty(orderLogPageInfo.getOrderNo())){
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("订单编号不能为空");
                    return resultObjectVO;
                }
                PageInfo<OrderLogVO> orderItemPage = orderLogService.queryOrderListPage(orderLogPageInfo);
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




}
