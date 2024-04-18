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
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.entity.OrderItem;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderExpressDelivery")
public class OrderExpressDeliveryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private OrderExpressDeliveryService orderExpressDeliveryService;




    /**
     * 保存或修改订单快递信息
     */
    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {

            OrderExpressDeliveryVO orderExpressDeliveryVO =requestJsonVO.formatEntity(OrderExpressDeliveryVO.class);
            if(orderExpressDeliveryVO.getOrderId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到订单");
                return resultObjectVO;
            }

            try {
                OrderExpressDelivery orderExpressDelivery = orderExpressDeliveryService.queryByOrderId(orderExpressDeliveryVO.getOrderId());
                if(orderExpressDelivery==null){
                    orderExpressDeliveryService.save(orderExpressDeliveryVO);
                }else{
                    orderExpressDelivery.setCourierNumber(orderExpressDeliveryVO.getCourierNumber());
                    orderExpressDelivery.setCompanyTypeCode(orderExpressDeliveryVO.getCompanyTypeCode());
                    orderExpressDelivery.setCompanyTypeName(orderExpressDeliveryVO.getCompanyTypeName());
                    orderExpressDeliveryService.update(orderExpressDelivery);
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
