package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.order.api.OrderItemServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 订单项列表
 */
@Controller
@RequestMapping("/order/orderItem")
public class OrderItemController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private OrderItemServiceAPI orderItemService;

    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, OrderItemPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo==null)
            {
                pageInfo = new OrderItemPageInfo();
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = orderItemService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<OrderItemVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), OrderItemVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        for(OrderItemVO orderItemVO:list)
                        {
                            if(StringUtils.isNotEmpty(orderItemVO.getProductPreviewPath()))
                            {
                                orderItemVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+orderItemVO.getProductPreviewPath());
                            }
                        }
                    }
                    tableVO.setData((List)list);
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }

    /**
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/all/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO queryListAllByOrderId(HttpServletRequest request,OrderItemPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            OrderItemVO queryOrderItemVO = new OrderItemVO();
            queryOrderItemVO.setOrderId(pageInfo.getOrderId());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryOrderItemVO);
            ResultObjectVO resultObjectVO = orderItemService.queryAllListByOrderId(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    List<OrderItemVO> list = resultObjectVO.formatDataList(OrderItemVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        tableVO.setCount(Long.parseLong(String.valueOf(list.size())));
                        tableVO.setData(list);
                        for(OrderItemVO orderItemVO:list)
                        {
                            if(StringUtils.isNotEmpty(orderItemVO.getProductPreviewPath()))
                            {
                                orderItemVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+orderItemVO.getProductPreviewPath());
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }


    /**
     * 修改
     * @param itemVOS
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/updatesFromOrderList",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updatesFromOrderList(HttpServletRequest request, @RequestBody List<OrderItemVO> itemVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, itemVOS);
            resultObjectVO = orderItemService.updatesFromOrderList(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("修改失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

