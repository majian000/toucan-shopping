package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.order.api.OrderExpressDeliveryServiceAPI;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.constant.OrderDictConstant;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 订单列表 - 页面控制器
 */
@Controller
public class OrderPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private OrderServiceAPI orderService;

    @Autowired
    private OrderExpressDeliveryServiceAPI orderExpressDeliveryService;

    @Autowired
    private DictServiceAPI dictServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/order/listPage", functionServiceAPI);
        return "pages/order/list.html";
    }



    /**
     * 查看
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            OrderVO query = new OrderVO();
            query.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            ResultObjectVO resultObjectVO = orderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                OrderVO orderVO = resultObjectVO.formatData(OrderVO.class);

                OrderExpressDeliveryVO orderExpressDeliveryVO=new OrderExpressDeliveryVO();
                orderExpressDeliveryVO.setOrderId(orderVO.getId());
                orderExpressDeliveryVO.setShopId(orderVO.getShopId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), orderExpressDeliveryVO);
                ResultTypeObjectVO<OrderExpressDeliveryVO> orderExpressDeliveryResultObjectVO = orderExpressDeliveryService.findOneByOrderIdAndShopId(requestJsonVO);
                if(orderExpressDeliveryResultObjectVO.isSuccess()){
                    orderVO.setOrderExpressDelivery(orderExpressDeliveryResultObjectVO.getData());
                }
                request.setAttribute("model",orderVO);

                setOrderDictList(request);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/order/detail.html";
    }



    /**
     * 修改
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            OrderVO query = new OrderVO();
            query.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            ResultObjectVO resultObjectVO = orderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                OrderVO orderVO = resultObjectVO.formatData(OrderVO.class);

                OrderExpressDeliveryVO orderExpressDeliveryVO=new OrderExpressDeliveryVO();
                orderExpressDeliveryVO.setOrderId(orderVO.getId());
                orderExpressDeliveryVO.setShopId(orderVO.getShopId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), orderExpressDeliveryVO);
                ResultTypeObjectVO<OrderExpressDeliveryVO> orderExpressDeliveryResultObjectVO = orderExpressDeliveryService.findOneByOrderIdAndShopId(requestJsonVO);
                if(orderExpressDeliveryResultObjectVO.isSuccess()){
                    orderVO.setOrderExpressDelivery(orderExpressDeliveryResultObjectVO.getData());
                }

                request.setAttribute("model",orderVO);

                setOrderDictList(request);

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/order/edit.html";
    }

    private void setOrderDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        //交易状态
        DictVO queryDict=new DictVO();
        queryDict.setCategoryCode(OrderDictConstant.ORDER_LOG_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(OrderDictConstant.ORDER_TRADE_STATUS_CODE);
        queryDict.getCodes().add(OrderDictConstant.ORDER_PAY_STATUS_CODE);
        queryDict.getCodes().add(OrderDictConstant.ORDER_PAY_METHOD_CODE);
        queryDict.getCodes().add(OrderDictConstant.ORDER_PAY_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if(resultObjectVO.isSuccess()) {
            if(!CollectionUtils.isEmpty(resultObjectVO.getData())){
                for(DictVO dictVO:resultObjectVO.getData()){
                    switch (dictVO.getCode()){
                        case OrderDictConstant.ORDER_TRADE_STATUS_CODE:
                            request.setAttribute("tradeStatusList",dictVO.getChildren());
                            break;
                        case OrderDictConstant.ORDER_PAY_STATUS_CODE:
                            request.setAttribute("payStatusList",dictVO.getChildren());
                            break;
                        case OrderDictConstant.ORDER_PAY_METHOD_CODE:
                            request.setAttribute("payMethodList",dictVO.getChildren());
                            break;
                        case OrderDictConstant.ORDER_PAY_TYPE_CODE:
                            request.setAttribute("payTypeList",dictVO.getChildren());
                            break;
                    }
                }
            }
        }

    }



    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/cancel/page/{orderNo}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String orderNo)
    {
        request.setAttribute("orderNo",orderNo);
        return "pages/order/cancel.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/orderItemListPage/{orderId}",method = RequestMethod.GET)
    public String spuListPage(HttpServletRequest request,@PathVariable String orderId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/order/orderItemListPage", functionServiceAPI);

        request.setAttribute("orderId",orderId);
        return "pages/order/modify_order_item_list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/order/orderLogListPage/{orderNo}",method = RequestMethod.GET)
    public String orderLogListPage(HttpServletRequest request,@PathVariable String orderNo)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/order/orderLogListPage", functionServiceAPI);

        request.setAttribute("orderNo",orderNo);
        return "pages/order/order_log_list.html";
    }

}
